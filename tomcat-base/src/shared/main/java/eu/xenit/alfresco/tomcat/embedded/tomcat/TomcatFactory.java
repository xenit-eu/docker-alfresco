package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.logging.json.valve.JsonAccessLogValve;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Protocol;
import org.apache.naming.resources.VirtualDirContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static eu.xenit.alfresco.tomcat.embedded.utils.Utils.redirectLog4j;

public class TomcatFactory {

    private final TomcatConfiguration configuration;

    public TomcatFactory(TomcatConfiguration configuration) {
        this.configuration = configuration;
    }

    public static Connector getConnector(Tomcat tomcat, String protocol, int port, boolean sslEnabled, String scheme, int maxThreads, int maxHttpHeaderSize, String relaxedPathChars, String relaxedQueryChars) {
        Connector connector = new Connector(protocol);
        connector.setPort(port);
        connector.setProperty("connectionTimeout", "240000");
        connector.setURIEncoding(StandardCharsets.UTF_8.name());
        connector.setProperty("SSLEnabled", String.valueOf(sslEnabled));
        connector.setProperty("maxThreads", String.valueOf(maxThreads));
        connector.setProperty("maxHttpHeaderSize", String.valueOf(maxHttpHeaderSize));
        connector.setProperty("relaxedPathChars", relaxedPathChars);
        connector.setProperty("relaxedQueryChars", relaxedQueryChars);
        connector.setScheme(scheme);
        Service service = tomcat.getService();
        service.setContainer(tomcat.getEngine());
        connector.setService(service);
        return connector;
    }

    private TomcatConfiguration getConfiguration() {
        return configuration;
    }

    public Tomcat getTomcat() throws IOException {
        Tomcat tomcat = new Tomcat();
        Host host = tomcat.getHost();
        host.setName("localhost");
        tomcat.setBaseDir(getConfiguration().getTomcatBaseDir());
        tomcat.setPort(getConfiguration().getTomcatPort());
        tomcat.getServer().setPort(getConfiguration().getTomcatServerPort());
        configureDefaultConnector(tomcat);
        addUserWithRole(tomcat, "CN=Alfresco Repository Client, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repoclient");
        addUserWithRole(tomcat, "CN=Alfresco Repository, OU=Unknown, O=Alfresco Software Ltd., L=Maidenhead, ST=UK, C=GB", null, "repository");
        Path webapps = Paths.get(getConfiguration().getWebappsPath());
        if (Files.exists(webapps)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(webapps)) {
                directoryStream.forEach(path -> addWebapp(tomcat, path));
            }
        }
        return tomcat;
    }

    protected boolean isEmptyDir(Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try (Stream<Path> entries = Files.list(path)) {
            return !entries.findAny().isPresent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                    // Create a standard context (assuming you already have it as 'ctx')
                    WebappLoader webappLoader = new WebappLoader();

                    // Add classpath directories
                    webappLoader.addRepository("file:" + getConfiguration().getSharedClasspathDir() + "/");
                    webappLoader.addRepository("file:" + getConfiguration().getGeneratedClasspathDir() + "/");
                    ctx.setLoader(webappLoader);

// Additional resources (like JARs) can be added using a virtual loader
                    VirtualDirContext resources = new VirtualDirContext();
                    resources.setExtraResourcePaths("/WEB-INF/lib=" + getConfiguration().getSharedLibDir());

                    if (getConfiguration().isJsonLogging()) {
                        redirectLog4j(path, Paths.get(configuration.getGeneratedClasspathDir()));
                    }
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

    private void configureDefaultConnector(Tomcat tomcat) {
        Connector connector = tomcat.getConnector();
        connector.setProperty("connectionTimeout", "240000");
        connector.setURIEncoding(StandardCharsets.UTF_8.name());
        Http11Protocol protocol = (Http11Protocol) connector.getProtocolHandler();
        protocol.setMaxThreads(getConfiguration().getTomcatMaxThreads());
        protocol.setMaxHttpHeaderSize(getConfiguration().getTomcatMaxHttpHeaderSize());
        protocol.setRelaxedPathChars(getConfiguration().getTomcatRelaxedPathChars());
        protocol.setRelaxedQueryChars(getConfiguration().getTomcatRelaxedQueryChars());
    }

    private void addUserWithRole(Tomcat tomcat, String username, String password, String role) {
        tomcat.addUser(username, password);
        tomcat.addRole(username, role);
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