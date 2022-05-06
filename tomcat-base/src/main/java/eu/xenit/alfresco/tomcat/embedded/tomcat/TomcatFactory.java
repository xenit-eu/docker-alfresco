package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.valve.JsonAccessLogValve;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TomcatFactory {

    private Configuration configuration;

    public TomcatFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Tomcat getTomcat() throws IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(configuration.getPort());
        tomcat.getConnector();

        Path webapps = Paths.get(configuration.getWebappsPath());
        if (Files.exists(webapps)) {
            Files.newDirectoryStream(webapps).forEach(path -> {
                        if (Files.isDirectory(path)) {
                            String contextPath = "/" + path.getFileName().toString();
                            String absolutePath = path.toAbsolutePath().toString().toString();
                            StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath,
                                    absolutePath);
                            ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());

                            LifecycleListener lifecycleListener = event -> {
                                if (event.getType().equals("before_start") && configuration.isEnableJsonLogging()) {
                                    redirectLog4j(path);
                                    WebResourceRoot resources = new StandardRoot(ctx);
                                    resources.addJarResources(new DirResourceSet(resources, "/WEB-INF/lib",
                                            configuration.getLogLibraryDir(), "/"));
                                    ctx.setResources(resources);
                                }
                                if (event.getType().equals("after_stop")) {
                                    stopTomcat(tomcat);
                                }
                            };
                            ctx.addLifecycleListener(lifecycleListener);

                            Valve valve = new JsonAccessLogValve();
                            ctx.addValve(valve);
                            ctx.getAccessLog();

                        }
                    }
            );
        }
        
        return tomcat;
    }

    private void redirectLog4j(Path path) {
        Path log4JPropertiesPath = path.resolve("WEB-INF/classes/log4j.properties");
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(log4JPropertiesPath));
            properties.setProperty("log4j.rootLogger", "error, Console, jmxlogger1");
            properties.setProperty("log4j.appender.Console.layout", "biz.paluch.logging.gelf.log4j.GelfLayout");
            properties.setProperty("log4j.appender.Console.layout.AdditionalFields",
                    "type=application/" + path.getFileName());
            properties.setProperty("log4j.appender.Console.layout.TimestampPattern", "yyyy-MM-dd HH:mm:ss,SSS");
            properties.setProperty("log4j.appender.Console.layout.ExtractStackTrace", "true");
            properties.setProperty("log4j.appender.Console.layout.FilterStackTrace", "true");
            Path tempProps = Files.createTempFile("log4j-", ".properties");
            try (OutputStream os = Files.newOutputStream(tempProps)) {
                properties.store(os, null);
            }
            System.setProperty("log4j.configuration", "file:" + tempProps.toAbsolutePath());
        } catch (IOException e) {

        }
    }

    private void stopTomcat(Tomcat tomcat) {
        Thread thread = new Thread(() -> {
            try {
                tomcat.stop();
                tomcat.destroy();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
