package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_URL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_SSL;
import static eu.xenit.alfresco.tomcat.embedded.test.utils.TestUtils.setEnv;
import static eu.xenit.alfresco.tomcat.embedded.test.utils.TestUtils.unsetEnv;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
