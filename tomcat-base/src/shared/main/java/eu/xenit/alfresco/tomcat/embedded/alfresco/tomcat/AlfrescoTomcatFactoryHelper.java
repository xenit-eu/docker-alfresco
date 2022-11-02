package eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.net.SSLHostConfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import static eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory.getConnector;

public class AlfrescoTomcatFactoryHelper {
    private AlfrescoTomcatFactoryHelper(){}
    private static final Logger LOG = Logger.getLogger(AlfrescoTomcatFactoryHelper.class.getName());


    public static Path createGlobalPropertiesFile(AlfrescoConfiguration alfrescoConfiguration) {
        Properties globalProperties = new Properties();
        globalProperties.putAll(alfrescoConfiguration.getGlobalProperties());
        try {
            Path tempProps = Paths.get(alfrescoConfiguration.getGeneratedClasspathDir(), "alfresco-global.properties");
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

        Connector connector = getConnector(tomcat, "org.apache.coyote.http11.Http11NioProtocol", alfrescoConfiguration.getTomcatSslPort(), true, "https", alfrescoConfiguration);

        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setCertificateKeystoreFile(alfrescoConfiguration.getTomcatSSLKeystore());
        sslHostConfig.setCertificateKeystorePassword(alfrescoConfiguration.getTomcatSSLKeystorePassword());
        sslHostConfig.setCertificateKeystoreType("JCEKS");
        sslHostConfig.setTruststoreFile(alfrescoConfiguration.getTomcatSSLTruststore());
        sslHostConfig.setTruststorePassword(alfrescoConfiguration.getTomcatSSLTruststorePassword());
        sslHostConfig.setTruststoreType("JCEKS");
        sslHostConfig.setSslProtocol("TLS");
        sslHostConfig.setCertificateVerification(SSLHostConfig.CertificateVerification.REQUIRED.name());
        connector.addSslHostConfig(sslHostConfig);
        connector.setSecure(true);
        connector.setProperty("clientAuth", "want");
        connector.setProperty("allowUnsafeLegacyRenegotiation", "true");
        connector.setMaxSavePostSize(-1);
        tomcat.setConnector(connector);
    }
}
