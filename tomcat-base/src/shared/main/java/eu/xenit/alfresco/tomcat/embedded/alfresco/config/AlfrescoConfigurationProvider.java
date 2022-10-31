package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


public interface AlfrescoConfigurationProvider {

    default AlfrescoConfiguration getConfiguration() {
        return getConfiguration(new AlfrescoConfiguration());
    }

    AlfrescoConfiguration getConfiguration(AlfrescoConfiguration baseConfiguration);


}
