package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.Main;
import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.valve.JsonAccessLogValve;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.util.TLSUtil;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.net.SSLHostConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TomcatFactory {

    private static final Logger LOG = Logger.getLogger(TomcatFactory.class.getName());

    private Configuration configuration;

    public TomcatFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Tomcat getTomcat() throws IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(configuration.getPort());
//        tomcat.getConnector();
        try {
            tomcat.setConnector(createConnector());
        }
        catch (Exception ex) {
            LOG.log(Level.INFO, "Creating connector failed. Using default connector instead", ex);
            tomcat.getConnector();
        }
        addUserWithRole(tomcat, "CN=Alfresco Repository Client, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repoclient");
        addUserWithRole(tomcat, "CN=Alfresco Repository, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repository");

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
                                    //Load extra jars in classpath for json application logging
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

    private Connector createConnector() {
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(configuration.getPort());
        connector.setURIEncoding(StandardCharsets.UTF_8.name());
        connector.setProperty("SSLEnabled", "true");
        connector.setProperty("maxThreads", "something");
        connector.setScheme("https");
        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setCertificateKeystoreFile("testpath");
        sslHostConfig.setCertificateKeystorePassword("testpassword");
        sslHostConfig.setCertificateKeystoreType("testype");
        sslHostConfig.setTruststoreFile("testpath");
        sslHostConfig.setTruststorePassword("testpassword");
        sslHostConfig.setTruststoreType("testtype");
        sslHostConfig.setSslProtocol("TLS");
        connector.addSslHostConfig(sslHostConfig);
        connector.setSecure(true);
        connector.setProperty("connectionTimeout", "240000");
        connector.setProperty("clientAuth", "want");
        connector.setProperty("allowUnsafeLegacyRenegotiation", "true");
        connector.setProperty("maxHttpHeaderSize", "TOMCAT_MAX_HTTP_HEADER_SIZE");
        connector.setMaxSavePostSize(-1);

        return connector;
    }

    private void addUserWithRole(Tomcat tomcat, String username, String password, String role) {
        tomcat.addUser(username, password);
        tomcat.addRole(username, role);
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
