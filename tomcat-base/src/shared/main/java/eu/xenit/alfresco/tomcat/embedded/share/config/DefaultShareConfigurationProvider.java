package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

public class DefaultShareConfigurationProvider implements ShareConfigurationProvider {

    @Override
    public ShareConfiguration getConfiguration(Configuration baseConfiguration) {
        ShareConfiguration baseShareConfiguration = (ShareConfiguration) baseConfiguration;
        baseShareConfiguration.setAlfrescoHost("alfresco");
        baseShareConfiguration.setAlfrescoPort(8080);
        baseShareConfiguration.setAlfrescoProtocol("http");
        baseShareConfiguration.setAlfrescoContext("alfresco");
        baseShareConfiguration.setAlfrescoInternalHost("alfresco");
        baseShareConfiguration.setAlfrescoInternalPort(8080);
        baseShareConfiguration.setAlfrescoInternalProtocol("http");
        baseShareConfiguration.setAlfrescoInternalContext("alfresco");
        return baseShareConfiguration;
    }
}
