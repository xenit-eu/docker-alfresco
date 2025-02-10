package eu.xenit.alfresco.tomcat.embedded.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DefaultConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        TomcatConfiguration configuration = new DefaultConfigurationProvider().getConfiguration();
        TomcatConfiguration expected = new TomcatConfiguration();
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
        expected.setTomcatCacheMaxSize(100000);
        expected.setAllowCasualMultipartParsing(false);
        expected.setAllowMultipleLeadingForwardSlashInPath(true);
        expected.setCrossContext(true);
        assertEquals(configuration, expected);
    }


}
