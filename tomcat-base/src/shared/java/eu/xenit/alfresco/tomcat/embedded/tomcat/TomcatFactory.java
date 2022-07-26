package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.valve.JsonAccessLogValve;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfig.CertificateVerification;

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
            Files.newDirectoryStream(webapps).forEach(path -> {
                        if (Files.isDirectory(path)) {
                            String contextPath = "/" + path.getFileName().toString();
                            String absolutePath = path.toAbsolutePath().toString();
                            StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath,
                                    absolutePath);
                            ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());

                            LifecycleListener lifecycleListener = event -> {
                                if (event.getType().equals("before_start") && configuration.isJsonLogging()) {
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

                            if (configuration.isAccessLogging()) {
                                Valve valve = new JsonAccessLogValve();
                                ctx.addValve(valve);
                                ctx.getAccessLog();
                            }
                        }
                    }
            );
        }
        
        return tomcat;
    }

    private void createDefaultConnector(Tomcat tomcat) {
        Connector connector = getConnector(tomcat, "HTTP/1.1", configuration.getPort(), false, "http");
        connector.setRedirectPort(configuration.getTomcatSslPort());
        tomcat.setConnector(connector);
    }

    private void createSSLConnector(Tomcat tomcat) {
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
