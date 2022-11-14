package eu.xenit.alfresco.tomcat.embedded.alfresco.config;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultAlfrescoConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        AlfrescoConfiguration configuration = new DefaultAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        expected.setTomcatSSLKeystore("/keystore/ssl.keystore");
        expected.setTomcatSSLTruststore("/keystore/ssl.truststore");
        expected.setTomcatSSLKeystorePassword("kT9X6oe68t");
        expected.setTomcatSSLTruststorePassword("kT9X6oe68t");
        expected.setSolrSSLEnabled(true);
        expected.setGlobalProperty("db.host", "postgresql");
        expected.setGlobalProperty("db.port", "5432");
        expected.setGlobalProperty("db.driver", "org.postgresql.Driver");
        expected.setGlobalProperty("db.user", "alfresco");
        expected.setGlobalProperty("db.password", "admin");
        expected.setGlobalProperty("db.url", "jdbc:postgresql://${db.host}:${db.port}/${db.name}");
        expected.setSystemProperty("encryption.keystore.type", "JCEKS");
        expected.setSystemProperty("encryption.cipherAlgorithm", "DESede/CBC/PKCS5Padding");
        expected.setSystemProperty("encryption.keyAlgorithm", "DESede");
        expected.setSystemProperty("encryption.keystore.location", "/keystore/keystore");
        expected.setSystemProperty("encryption.keystore.keyMetaData.location", "/keystore/keystore-passwords.properties");
        expected.setSystemProperty("metadata-keystore.password", "mp6yc0UD9e");
        expected.setSystemProperty("metadata-keystore.aliases", "metadata");
        expected.setSystemProperty("metadata-keystore.metadata.password", "oKIWzVdEdA");
        expected.setSystemProperty("metadata-keystore.metadata.algorithm", "DESede");
        expected.setGlobalProperty("encryption.ssl.keystore.location", "/keystore/ssl.keystore");
        expected.setGlobalProperty("encryption.ssl.keystore.keyMetaData.location", "/keystore/ssl-keystore-passwords.properties");
        expected.setGlobalProperty("encryption.ssl.keystore.type", "JCEKS");
        expected.setGlobalProperty("encryption.ssl.truststore.location", "/keystore/ssl.truststore");
        expected.setGlobalProperty("encryption.ssl.truststore.keyMetaData.location", "/keystore/ssl-truststore-passwords.properties");
        expected.setGlobalProperty("encryption.ssl.truststore.type", "JCEKS");
        expected.setSystemProperty("ssl-truststore.password", "kT9X6oe68t");
        expected.setSystemProperty("ssl-keystore.password", "kT9X6oe68t");
        expected.setGlobalProperty("messaging.subsystem.autoStart", "false");
        expected.setGlobalProperty("events.subsystem.autoStart", "false");
        expected.setGlobalProperty("local.transform.service.enabled", "true");
        expected.setGlobalProperty("index.subsystem.name", "solr6");
        expected.setGlobalProperty("solr.host", "solr");
        expected.setGlobalProperty("solr.port", "8080");
        expected.setGlobalProperty("solr.port.ssl", "8443");
        expected.setGlobalProperty("solr.secureComms", "https");
        expected.setGlobalProperty("dir.root", "/opt/alfresco/alf_data");
        assertEquals(configuration, expected);
    }
}
