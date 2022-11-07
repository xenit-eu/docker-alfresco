package eu.xenit.alfresco.tomcat.embedded.share.config;

public class DefaultShareConfigurationProvider implements ShareConfigurationProvider {

    @Override
    public ShareConfiguration getConfiguration(ShareConfiguration baseShareConfiguration) {
        baseShareConfiguration.setAlfrescoHost("alfresco");
        baseShareConfiguration.setAlfrescoPort(8080);
        baseShareConfiguration.setAlfrescoProtocol("http");
        baseShareConfiguration.setAlfrescoContext("alfresco");
        baseShareConfiguration.setAlfrescoInternalHost("alfresco");
        baseShareConfiguration.setAlfrescoInternalPort(8080);
        baseShareConfiguration.setAlfrescoInternalProtocol("http");
        baseShareConfiguration.setAlfrescoInternalContext("alfresco");
        baseShareConfiguration.setShareConfigTemplateFile("/docker-config/share-config-custom.xml");
        baseShareConfiguration.setShareConfigPath("alfresco/web-extension");
        return baseShareConfiguration;
    }
}
