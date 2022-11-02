package eu.xenit.alfresco.tomcat.embedded.config;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvironmentVariableConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        Configuration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        Configuration expected = new Configuration();
        assertEquals(configuration, expected);
    }
}
