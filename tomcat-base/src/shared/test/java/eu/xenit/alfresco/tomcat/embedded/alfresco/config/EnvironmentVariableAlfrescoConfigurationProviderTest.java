package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_URL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_SSL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentVariableAlfrescoConfigurationProviderTest {

    private final static String SECURE_COMMS_KEY = "solr.secureComms";
    private final static String SOLR_SECURE_COMMS_ENV = "GLOBAL_" + SECURE_COMMS_KEY;

    @Test
    void testGetConfiguration() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        assertEquals(expected, configuration);
    }

    @SetEnvironmentVariable(key = DB_URL, value = "test db url")
    @SetEnvironmentVariable(key = SOLR_SSL, value = "none")
    @Test
    void testGetConfigurationWithEnv() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        expected.setGlobalProperty("db.url", "test db url");
        expected.setGlobalProperty("solr.secureComms", "none");
        expected.setSolrSSLEnabled(false);
        assertEquals(expected, configuration);
    }

    @SetEnvironmentVariable(key = "GLOBAL_test", value = "test env")
    @Test
    void testGetConfigurationWithGlobalEnv() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        expected.setGlobalProperty("test", "test env");
        assertEquals(expected, configuration);
    }

    @SetEnvironmentVariable(key = SOLR_SSL, value = "https")
    @Test
    void testSolrSslSettings_SOLR_SSL_https() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertTrue(configuration.isSolrSSLEnabled());
        assertEquals("https", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }

    @SetEnvironmentVariable(key = SOLR_SSL, value = "none")
    @Test
    void testSolrSslSettings_SOLR_SSL_none() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertFalse(configuration.isSolrSSLEnabled());
        assertEquals("none", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }

    @SetEnvironmentVariable(key = SOLR_SSL, value = "secret")
    @Test
    void testSolrSslSettings_SOLR_SSL_secret() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertFalse(configuration.isSolrSSLEnabled());
        assertEquals("secret", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }

    @SetEnvironmentVariable(key = SOLR_SECURE_COMMS_ENV, value = "https")
    @Test
    void testSolrSslSettings_secureComms_https() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertTrue(configuration.isSolrSSLEnabled());
        assertEquals("https", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }

    @SetEnvironmentVariable(key = SOLR_SECURE_COMMS_ENV, value = "none")
    @Test
    void testSolrSslSettings_secureComms_none() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertFalse(configuration.isSolrSSLEnabled());
        assertEquals("none", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }

    @SetEnvironmentVariable(key = SOLR_SECURE_COMMS_ENV, value = "secret")
    @Test
    void testSolrSslSettings_secureComms_secret() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertFalse(configuration.isSolrSSLEnabled());
        assertEquals("secret", configuration.getGlobalProperties().get(SECURE_COMMS_KEY));
    }
}
