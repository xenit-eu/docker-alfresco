package eu.xenit.alfresco.tomcat.embedded.share.config;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnvironmentVariableShareConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        ShareConfiguration configuration = new EnvironmentVariableShareConfigurationProvider().getConfiguration();
        ShareConfiguration expected = new ShareConfiguration();
        assertEquals(configuration, expected);
    }
}
