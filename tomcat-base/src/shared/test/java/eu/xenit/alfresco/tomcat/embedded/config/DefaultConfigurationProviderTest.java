package eu.xenit.alfresco.tomcat.embedded.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        Configuration configuration = new DefaultConfigurationProvider().getConfiguration();
        Configuration expected = new Configuration();
        expected.setWebappsPath("/usr/local/tomcat/webapps");
        expected.setTomcatBaseDir("/usr/local/tomcat/temp");
        expected.setTomcatPort(8080);
        expected.setJsonLogging(false);
        expected.setAccessLogging(false);
        expected.setSharedLibDir("/usr/local/tomcat/shared/lib");
        expected.setTomcatMaxThreads(200);
        expected.setTomcatMaxHttpHeaderSize(32768);
        expected.setTomcatSslPort(8443);
        expected.setTomcatServerPort(8005);
        expected.setExitOnFailure(true);
        expected.setAlfrescoEnabled(false);
        expected.setShareEnabled(false);
        expected.setGeneratedClasspathDir("/dev/shm/classpath");
        expected.setSharedClasspathDir("/usr/local/tomcat/shared/classes");
        assertEquals(configuration, expected);
    }


}
