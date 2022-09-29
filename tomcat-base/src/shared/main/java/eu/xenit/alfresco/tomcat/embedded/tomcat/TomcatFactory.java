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
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfig.CertificateVerification;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TomcatFactory {

    private static final Logger LOG = Logger.getLogger(TomcatFactory.class.getName());

    private Configuration configuration;

    public TomcatFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Tomcat getTomcat() throws IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(configuration.getPort());
        tomcat.getServer().setPort(configuration.getTomcatServerPort());
        createDefaultConnector(tomcat);
        if (configuration.isSolrSSLEnabled()) {
            createSSLConnector(tomcat);
        }
        addUserWithRole(tomcat, "CN=Alfresco Repository Client, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repoclient");
        addUserWithRole(tomcat, "CN=Alfresco Repository, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repository");

        Path webapps = Paths.get(configuration.getWebappsPath());
        if (Files.exists(webapps)) {
            try (var directoryStream = Files.newDirectoryStream(webapps)) {
                directoryStream.forEach(path -> addWebapp(tomcat, path));
            }
        }

        return tomcat;
    }

    private boolean isEmtpyDir(Path path) {
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
        if (isEmtpyDir(path)) {
            // Our gradle plugin adds a share directory, even when baseShareWar is not configured
            return;
        }
        if (Files.isDirectory(path)) {
            String contextPath = "/" + path.getFileName().toString();
            String absolutePath = path.toAbsolutePath().toString();
            StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath,
                    absolutePath);
            ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());

            LifecycleListener lifecycleListener = event -> {
                if (event.getType().equals("before_start")) {
                    WebResourceRoot resources = new StandardRoot(ctx);
                    Path globalPropertiesFile = getGlobalPropertiesFile();
                    resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", globalPropertiesFile.toAbsolutePath().getParent().toString(), "/"));
                    if (configuration.isJsonLogging()) {
                        redirectLog4j(path);
                        //Load extra jars in classpath for json application logging
                        resources.addJarResources(new DirResourceSet(resources, "/WEB-INF/lib",
                                configuration.getLogLibraryDir(), "/"));
                    }
                    ctx.setResources(resources);
                }
                if (configuration.isExitOnFailure() && event.getType().equals("after_stop")) {
                    stopTomcat(tomcat);
                }
            };
            ctx.addLifecycleListener(lifecycleListener);

            if (configuration.isAccessLogging()) {
                JsonAccessLogValve valve = new JsonAccessLogValve(System.out);
                ctx.addValve(valve);
                ctx.getAccessLog();
            }
        }
    }

    private Path getGlobalPropertiesFile() {
        Properties globalProperties = new Properties();
        configuration.getGlobalProperties().forEach((key, value) -> globalProperties.put(key, value));
        Path classesDir = Paths.get("/dev", "shm", "alfrescoClasses");
        try {
            Files.createDirectories(classesDir);
            Path tempProps = Paths.get("/dev", "shm", "alfrescoClasses", "alfresco-global.properties");
            if (Files.exists(tempProps)) {
                Files.delete(tempProps);
            }
            tempProps = Files.createFile(tempProps);
            try (OutputStream os = Files.newOutputStream(tempProps)) {
                globalProperties.store(os, null);
            }
            return tempProps;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDefaultConnector(Tomcat tomcat) {
        Connector connector = getConnector(tomcat, "HTTP/1.1", configuration.getPort(), false, "http");
        connector.setRedirectPort(configuration.getTomcatSslPort());
        tomcat.setConnector(connector);
    }

    private void createSSLConnector(Tomcat tomcat) {
        if (!new File(configuration.getTomcatSSLKeystore()).exists()) {
            LOG.severe("Keystore file missing: " + configuration.getTomcatSSLKeystore());
            System.exit(1);
        }

        if (!new File(configuration.getTomcatSSLTruststore()).exists()) {
            LOG.severe("Truststore file missing: " + configuration.getTomcatSSLTruststore());
            System.exit(1);
        }

        Connector connector = getConnector(tomcat, "org.apache.coyote.http11.Http11NioProtocol", configuration.getTomcatSslPort(), true, "https");

        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setCertificateKeystoreFile(configuration.getTomcatSSLKeystore());
        sslHostConfig.setCertificateKeystorePassword(configuration.getTomcatSSLKeystorePassword());
        sslHostConfig.setCertificateKeystoreType("JCEKS");
        sslHostConfig.setTruststoreFile(configuration.getTomcatSSLTruststore());
        sslHostConfig.setTruststorePassword(configuration.getTomcatSSLTruststorePassword());
        sslHostConfig.setTruststoreType("JCEKS");
        sslHostConfig.setSslProtocol("TLS");
        sslHostConfig.setCertificateVerification(CertificateVerification.REQUIRED.name());
        connector.addSslHostConfig(sslHostConfig);
        connector.setSecure(true);
        connector.setProperty("clientAuth", "want");
        connector.setProperty("allowUnsafeLegacyRenegotiation", "true");
        connector.setMaxSavePostSize(-1);
        tomcat.setConnector(connector);
    }

    private Connector getConnector(Tomcat tomcat, String protocol, int port, boolean sslEnabled, String scheme) {
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

    private void redirectLog4j(Path path) {
        Path log4JPropertiesPath = path.resolve("WEB-INF/classes/log4j.properties");
        Properties properties = new Properties();
        try {
            try (var reader = Files.newBufferedReader(log4JPropertiesPath)) {
                properties.load(reader);
                properties.setProperty("log4j.rootLogger", "error, Console, jmxlogger1");
                properties.setProperty("log4j.appender.Console.layout", "eu.xenit.json.log4j.JsonFormatter");
                properties.setProperty("log4j.appender.Console.layout.Type", "application");
                properties.setProperty("log4j.appender.Console.layout.Component", path.getFileName().toString());
                properties.setProperty("log4j.appender.Console.layout.TimestampPattern", "yyyy-MM-dd HH:mm:ss,SSS");
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
