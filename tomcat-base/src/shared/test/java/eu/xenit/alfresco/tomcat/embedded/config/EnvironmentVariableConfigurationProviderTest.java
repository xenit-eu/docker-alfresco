package eu.xenit.alfresco.tomcat.embedded.config;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvironmentVariableConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
        assertEquals(configuration, expected);
    }
}
