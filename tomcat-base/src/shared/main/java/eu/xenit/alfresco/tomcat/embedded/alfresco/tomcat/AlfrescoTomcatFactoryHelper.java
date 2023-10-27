package eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class AlfrescoTomcatFactoryHelper {

    private static final Logger LOG = Logger.getLogger(AlfrescoTomcatFactoryHelper.class.getName());

    private AlfrescoTomcatFactoryHelper() {
    }

    public static Path createGlobalPropertiesFile(AlfrescoConfiguration alfrescoConfiguration) {
        Properties globalProperties = new Properties();
        globalProperties.putAll(alfrescoConfiguration.getGlobalProperties());
        try {
            Path tempProps = Paths.get(alfrescoConfiguration.getTomcatConfiguration().getGeneratedClasspathDir(),
                    "alfresco-global.properties");
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


    public static void createSSLConnector(Tomcat tomcat, AlfrescoConfiguration alfrescoConfiguration) {
        if (!new File(alfrescoConfiguration.getTomcatSSLKeystore()).exists()) {
            LOG.severe("Keystore file missing: " + alfrescoConfiguration.getTomcatSSLKeystore());
            System.exit(1);
        }

        if (!new File(alfrescoConfiguration.getTomcatSSLTruststore()).exists()) {
            LOG.severe("Truststore file missing: " + alfrescoConfiguration.getTomcatSSLTruststore());
            System.exit(1);
        }

        TomcatConfiguration tomcatConfiguration = alfrescoConfiguration.getTomcatConfiguration();
        Connector connector = TomcatFactory.getConnector(tomcat,
                "org.apache.coyote.http11.Http11NioProtocol",
                tomcatConfiguration.getTomcatSslPort(),
                true,
                "https",
                tomcatConfiguration.getTomcatMaxThreads(),
                tomcatConfiguration.getTomcatMaxHttpHeaderSize(),
                tomcatConfiguration.getTomcatRelaxedPathChars(),
                tomcatConfiguration.getTomcatRelaxedQueryChars()
        );

        addSslHost(alfrescoConfiguration, connector);
        connector.setSecure(true);
        connector.setProperty("clientAuth", "want");
        connector.setProperty("allowUnsafeLegacyRenegotiation", "true");
        connector.setMaxSavePostSize(-1);
        tomcat.setConnector(connector);
    }

    private static void addSslHost(AlfrescoConfiguration alfrescoConfiguration, Connector connector) {

        // Assuming you already have the Connector object initialized as 'connector'
        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort(8443); // or any other port you want to use for SSL

// Set the SSL protocol
        connector.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
        connector.setAttribute("SSLEnabled", true);
        connector.setAttribute("sslProtocol", "TLS");
        connector.setAttribute("clientAuth", "want");

// Keystore configurations
        connector.setAttribute("keystoreFile", alfrescoConfiguration.getTomcatSSLKeystore());
        connector.setAttribute("keystorePass", alfrescoConfiguration.getTomcatSSLKeystorePassword());
        connector.setAttribute("keystoreType", "JCEKS");

// Truststore configurations
        connector.setAttribute("truststoreFile", alfrescoConfiguration.getTomcatSSLTruststore());
        connector.setAttribute("truststorePass", alfrescoConfiguration.getTomcatSSLTruststorePassword());
        connector.setAttribute("truststoreType", "JCEKS");

// Other properties
        connector.setAttribute("allowUnsafeLegacyRenegotiation", "true");
        connector.setMaxSavePostSize(-1);
    }
}
