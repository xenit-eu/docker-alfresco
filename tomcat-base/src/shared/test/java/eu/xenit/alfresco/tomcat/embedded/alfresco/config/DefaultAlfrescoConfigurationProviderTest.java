package eu.xenit.alfresco.tomcat.embedded.alfresco.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

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
        expected.setGlobalProperty("db.username", "alfresco");
        expected.setGlobalProperty("db.name", "alfresco");
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

    @Test
    @SetEnvironmentVariable(key = "ALFRESCO_VERSION", value = "25.4.1")
    void testLegacySolrDefaults() {
        AlfrescoConfiguration configuration = new DefaultAlfrescoConfigurationProvider().getConfiguration();
        assertEquals("solr6", configuration.getGlobalProperties().get("index.subsystem.name"));
        assertEquals("https", configuration.getGlobalProperties().get("solr.secureComms"));
        assertTrue(configuration.isSolrSSLEnabled());
    }

    @Test
    @SetEnvironmentVariable(key = "ALFRESCO_VERSION", value = "26.2.2")
    void testAlfresco26UsesOpenSearch() {
        AlfrescoConfiguration configuration = new DefaultAlfrescoConfigurationProvider().getConfiguration();
        assertFalse(configuration.isSolrSSLEnabled());
        assertEquals("elasticsearch", configuration.getGlobalProperties().get("index.subsystem.name"));
        assertEquals("opensearch", configuration.getGlobalProperties().get("elasticsearch.host"));
        assertEquals("9200", configuration.getGlobalProperties().get("elasticsearch.port"));
        assertEquals("none", configuration.getGlobalProperties().get("elasticsearch.secureComms"));
        assertEquals("true", configuration.getGlobalProperties().get("elasticsearch.createIndexIfNotExists"));
        assertFalse(configuration.getGlobalProperties().keySet().stream().anyMatch(key -> key.startsWith("solr.")));
    }

    @Test
    @SetEnvironmentVariable(key = "ALFRESCO_VERSION", value = "26.2.0")
    void testAlfresco26CommunityUsesOpenSearch() {
        AlfrescoConfiguration configuration = new DefaultAlfrescoConfigurationProvider().getConfiguration();
        assertEquals("elasticsearch", configuration.getGlobalProperties().get("index.subsystem.name"));
        assertEquals("opensearch", configuration.getGlobalProperties().get("elasticsearch.host"));
        assertFalse(configuration.isSolrSSLEnabled());
    }

    @Test
    void testSetSystemVariable() {
        String expected = "testKeystore";
        String key = "encryption.keystore.location";
        System.setProperty(key, expected);
        new DefaultAlfrescoConfigurationProvider().getConfiguration();
        // Check that the custom setting is not overridden by the default configuration
        assertEquals(expected, System.getProperty(key));
    }
}
