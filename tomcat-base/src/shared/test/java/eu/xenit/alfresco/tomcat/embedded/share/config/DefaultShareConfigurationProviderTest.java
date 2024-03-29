package eu.xenit.alfresco.tomcat.embedded.share.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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

}
