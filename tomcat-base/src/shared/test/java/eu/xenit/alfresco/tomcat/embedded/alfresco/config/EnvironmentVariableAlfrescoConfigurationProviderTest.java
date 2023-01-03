package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_URL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_SSL;
import static eu.xenit.alfresco.tomcat.embedded.test.utils.TestUtils.setEnv;
import static eu.xenit.alfresco.tomcat.embedded.test.utils.TestUtils.unsetEnv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnvironmentVariableAlfrescoConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        assertEquals(expected, configuration);
    }

    @Test
    void testGetConfigurationWithEnv() throws Exception {
        setEnv(Map.of(DB_URL, "test db url", SOLR_SSL, "none"));
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        expected.setGlobalProperty("db.url", "test db url");
        expected.setGlobalProperty("solr.secureComms", "none");
        expected.setSolrSSLEnabled(false);
        assertEquals(expected, configuration);
        unsetEnv(Set.of(DB_URL, SOLR_SSL));
    }

    @Test
    void testGetConfigurationWithGlobalEnv() throws Exception {
        setEnv(Map.of("GLOBAL_test", "test env"));
        AlfrescoConfiguration configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        AlfrescoConfiguration expected = new AlfrescoConfiguration();
        expected.setGlobalProperty("test", "test env");
        assertEquals(expected, configuration);
        unsetEnv(Set.of("GLOBAL_test"));
    }

    @Test
    void testSolrSslSettings() throws Exception {
        var secureCommsKey = "solr.secureComms";

        setEnv(Map.of(SOLR_SSL, "https"));
        var configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(true, configuration.isSolrSSLEnabled());
        assertEquals("https", configuration.getGlobalProperties().get(secureCommsKey));

        setEnv(Map.of(SOLR_SSL, "none"));
        configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(false, configuration.isSolrSSLEnabled());
        assertEquals("none", configuration.getGlobalProperties().get(secureCommsKey));

        setEnv(Map.of(SOLR_SSL, "secret"));
        configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(false, configuration.isSolrSSLEnabled());
        assertEquals("secret", configuration.getGlobalProperties().get(secureCommsKey));

        unsetEnv(Set.of(SOLR_SSL));

        var solrSecureCommsEnv = "GLOBAL_"+secureCommsKey;
        setEnv(Map.of(solrSecureCommsEnv, "https"));
        configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(true, configuration.isSolrSSLEnabled());
        assertEquals("https", configuration.getGlobalProperties().get(secureCommsKey));

        setEnv(Map.of(solrSecureCommsEnv, "none"));
        configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(false, configuration.isSolrSSLEnabled());
        assertEquals("none", configuration.getGlobalProperties().get(secureCommsKey));

        setEnv(Map.of(solrSecureCommsEnv, "secret"));
        configuration = new EnvironmentVariableAlfrescoConfigurationProvider().getConfiguration();
        assertEquals(false, configuration.isSolrSSLEnabled());
        assertEquals("secret", configuration.getGlobalProperties().get(secureCommsKey));

        unsetEnv(Set.of(solrSecureCommsEnv));
    }
}
