package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.json.valve.JsonAccessLogValve;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Service;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TomcatFactory {

    private static final Logger LOG = Logger.getLogger(TomcatFactory.class.getName());


    public TomcatFactory(Configuration configuration) {
        this.configuration =
                configuration;
    }

    public List<Consumer<WebResourceRoot>> getWebResources() {
        return webResources;
    }

    public void setWebResources(Consumer<WebResourceRoot> webResource) {
        this.webResources.add(webResource);
    }

    private List<Consumer<WebResourceRoot>> webResources = new ArrayList<>();
    private final Configuration configuration;

    private Configuration getConfiguration() {
        return configuration;
    }

    public Tomcat getTomcat() throws IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(getConfiguration().getPort());
        tomcat.getServer().setPort(getConfiguration().getTomcatServerPort());
        createDefaultConnector(tomcat);
        addUserWithRole(tomcat, "CN=Alfresco Repository Client, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repoclient");
        addUserWithRole(tomcat, "CN=Alfresco Repository, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repository");
        Path webapps = Paths.get(getConfiguration().getWebappsPath());
        if (Files.exists(webapps)) {
            try (var directoryStream = Files.newDirectoryStream(webapps)) {
                directoryStream.forEach(path -> addWebapp(tomcat, path));
            }
        }

        return tomcat;
    }

    protected boolean isEmptyDir(Path path) {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return entries.findFirst().isEmpty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    private void addWebapp(Tomcat tomcat, Path path) {
        if (isEmptyDir(path)) {
            // Our gradle plugin adds a share directory, even when baseShareWar is not configured
            return;
        }
        if (Files.isDirectory(path)) {
            String contextPath = "/" + path.getFileName().toString();
            String absolutePath = path.toAbsolutePath().toString();
            StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath, absolutePath);
            ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());

            LifecycleListener lifecycleListener = event -> {
                if (event.getType().equals("before_start")) {
                    WebResourceRoot resources = new StandardRoot(ctx);
                    resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", "/dev/shm/classpath/", "/"));
                    resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", "/usr/local/tomcat/shared/classes/", "/"));

//                    getWebResources().forEach(webResourceRootConsumer -> webResourceRootConsumer.accept(resources));
//                    if (getConfiguration().isJsonLogging() && redirectLog4j(path)) {
                        //Load extra jars in classpath for json application logging
                    resources.addJarResources(new DirResourceSet(resources, "/WEB-INF/lib", getConfiguration().getSharedClassesPath(), "/"));
//                    }
                    ctx.setResources(resources);
                }
                if (getConfiguration().isExitOnFailure() && event.getType().equals("after_stop")) {
                    stopTomcat(tomcat);
                }
            };
            ctx.addLifecycleListener(lifecycleListener);

            if (getConfiguration().isAccessLogging()) {
                JsonAccessLogValve valve = new JsonAccessLogValve();
                ctx.addValve(valve);
                ctx.getAccessLog();
            }
        }
    }


    private void createDefaultConnector(Tomcat tomcat) {
        Connector connector = getConnector(tomcat, "HTTP/1.1", getConfiguration().getPort(), false, "http", configuration);
        connector.setRedirectPort(getConfiguration().getTomcatSslPort());
        tomcat.setConnector(connector);
    }


    public static Connector getConnector(Tomcat tomcat, String protocol, int port, boolean sslEnabled, String scheme, Configuration configuration) {
        Connector connector = new Connector(protocol);
        connector.setPort(port);
        connector.setProperty("connectionTimeout", "240000");
        connector.setURIEncoding(StandardCharsets.UTF_8.name());
        connector.setProperty("SSLEnabled", String.valueOf(sslEnabled));
        connector.setProperty("maxThreads", String.valueOf(configuration.getTomcatMaxThreads()));
        connector.setProperty("maxHttpHeaderSize", String.valueOf(configuration.getTomcatMaxHttpHeaderSize()));
        connector.setScheme(scheme);
        Service service = tomcat.getService();
        service.setContainer(tomcat.getEngine());
        connector.setService(service);
        return connector;
    }

    private void addUserWithRole(Tomcat tomcat, String username, String password, String role) {
        tomcat.addUser(username, password);
        tomcat.addRole(username, role);
    }

    private boolean redirectLog4j(Path path) {
        Path log4JPropertiesPath = path.resolve("WEB-INF/classes/log4j.properties");
        if (!Files.exists(log4JPropertiesPath)) {
            LOG.warning("Log4j file doesn't exist under path " + log4JPropertiesPath);
            return false;
        }
        Properties properties = new Properties();
        try {
            try (var reader = Files.newBufferedReader(log4JPropertiesPath)) {
                properties.load(reader);
                properties.setProperty("log4j.rootLogger", "error, Console, jmxlogger1");
                properties.setProperty("log4j.appender.Console.layout", "eu.xenit.json.log4j.JsonLayout");
                properties.setProperty("log4j.appender.Console.layout.Type", "application");
                properties.setProperty("log4j.appender.Console.layout.Component", path.getFileName().toString());
                properties.setProperty("log4j.appender.Console.layout.ExtractStackTrace", "true");
                properties.setProperty("log4j.appender.Console.layout.FilterStackTrace", "true");
                Path tempProps = Files.createTempFile("log4j-", ".properties");
                try (OutputStream os = Files.newOutputStream(tempProps)) {
                    properties.store(os, null);
                }
                System.setProperty("log4j.configuration", "file:" + tempProps.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
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