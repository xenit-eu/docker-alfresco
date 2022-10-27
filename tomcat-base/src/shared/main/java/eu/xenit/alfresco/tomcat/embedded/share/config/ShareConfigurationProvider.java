package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

public interface ShareConfigurationProvider {

    default ShareConfiguration getConfiguration() {
        return getConfiguration(new Configuration());
    }

    ShareConfiguration getConfiguration(Configuration baseShareConfiguration);
}
