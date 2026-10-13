package eu.xenit.alfresco.tomcat.embedded.config;


import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_MAX_PART_COUNT;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_MAX_PART_HEADER_SIZE;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_MAX_THREADS_SECONDARY;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_PORT_SECONDARY;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_SECONDARY_CONNECTOR_ENABLED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

class EnvironmentVariableConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
        assertEquals(configuration, expected);
    }

    @SetEnvironmentVariable(key = TOMCAT_MAX_PART_COUNT, value = "250")
    @SetEnvironmentVariable(key = TOMCAT_MAX_PART_HEADER_SIZE, value = "2048")
    @Test
    void testGetConfigurationWithMultipartEnv() {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
        expected.setTomcatMaxPartCount(250);
        expected.setTomcatMaxPartHeaderSize(2048);
        assertEquals(expected, configuration);
    }

    @SetEnvironmentVariable(key = TOMCAT_SECONDARY_CONNECTOR_ENABLED, value = "true")
    @SetEnvironmentVariable(key = TOMCAT_PORT_SECONDARY, value = "9090")
    @SetEnvironmentVariable(key = TOMCAT_MAX_THREADS_SECONDARY, value = "20")
    @Test
    void testGetConfigurationWithSecondaryConnectorEnv() {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
        expected.setSecondaryConnectorEnabled(true);
        expected.setTomcatSecondaryPort(9090);
        expected.setTomcatSecondaryMaxThreads(20);
        assertEquals(expected, configuration);
    }
}
