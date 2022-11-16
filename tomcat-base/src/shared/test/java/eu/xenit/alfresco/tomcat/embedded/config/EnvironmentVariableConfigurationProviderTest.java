package eu.xenit.alfresco.tomcat.embedded.config;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnvironmentVariableConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
        assertEquals(configuration, expected);
    }
}
