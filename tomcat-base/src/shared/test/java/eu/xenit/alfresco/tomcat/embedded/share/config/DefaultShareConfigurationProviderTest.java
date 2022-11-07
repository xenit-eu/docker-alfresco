package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultShareConfigurationProviderTest {
    @Test
    void testGetConfiguration() {
        ShareConfiguration configuration = new DefaultShareConfigurationProvider().getConfiguration();
        ShareConfiguration expected = new ShareConfiguration();
        expected.setAlfrescoHost("alfresco");
        expected.setAlfrescoPort(8080);
        expected.setAlfrescoProtocol("http");
        expected.setAlfrescoContext("alfresco");
        expected.setAlfrescoInternalHost("alfresco");
        expected.setAlfrescoInternalPort(8080);
        expected.setAlfrescoInternalProtocol("http");
        expected.setAlfrescoInternalContext("alfresco");
        expected.setShareConfigTemplateFile("/docker-config/share-config-custom.xml");
        expected.setShareConfigPath("alfresco/web-extension");
        assertEquals(configuration, expected);
    }

    @Test
    void testGetConfigurationFromCommon() {
        ShareConfiguration configuration = new DefaultShareConfigurationProvider().getConfiguration(new ShareConfiguration(new DefaultConfigurationProvider().getConfiguration()));
        ShareConfiguration expected = new ShareConfiguration();
        expected.setAlfrescoHost("alfresco");
        expected.setAlfrescoPort(8080);
        expected.setAlfrescoProtocol("http");
        expected.setAlfrescoContext("alfresco");
        expected.setAlfrescoInternalHost("alfresco");
        expected.setAlfrescoInternalPort(8080);
        expected.setAlfrescoInternalProtocol("http");
        expected.setAlfrescoInternalContext("alfresco");
        expected.setShareConfigTemplateFile("/docker-config/share-config-custom.xml");
        expected.setShareConfigPath("alfresco/web-extension");
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
        assertEquals(configuration, expected);
    }
}
