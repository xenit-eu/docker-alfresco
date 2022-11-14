package eu.xenit.alfresco.tomcat.embedded.share.config;

public class DefaultShareConfigurationProvider implements ShareConfigurationProvider {

    public static final String ALFRESCO_HOST = "alfresco";
    public static final int ALFRESCO_PORT = 8080;
    public static final String ALFRESCO_PROTOCOL = "http";

    @Override
    public ShareConfiguration getConfiguration(ShareConfiguration baseShareConfiguration) {
        baseShareConfiguration.setAlfrescoHost(ALFRESCO_HOST);
        baseShareConfiguration.setAlfrescoPort(ALFRESCO_PORT);
        baseShareConfiguration.setAlfrescoProtocol(ALFRESCO_PROTOCOL);
        baseShareConfiguration.setAlfrescoContext(ALFRESCO_HOST);
        baseShareConfiguration.setAlfrescoInternalHost(ALFRESCO_HOST);
        baseShareConfiguration.setAlfrescoInternalPort(ALFRESCO_PORT);
        baseShareConfiguration.setAlfrescoInternalProtocol(ALFRESCO_PROTOCOL);
        baseShareConfiguration.setAlfrescoInternalContext(ALFRESCO_HOST);
        baseShareConfiguration.setShareConfigTemplateFile("/docker-config/share-config-custom.xml");
        baseShareConfiguration.setShareConfigPath("alfresco/web-extension");
        return baseShareConfiguration;
    }
}
