package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

public interface AlfrescoConfigurationProvider {

    default AlfrescoConfiguration getConfiguration() {
        return getConfiguration(new Configuration());
    }

    AlfrescoConfiguration getConfiguration(Configuration baseConfiguration);


}
