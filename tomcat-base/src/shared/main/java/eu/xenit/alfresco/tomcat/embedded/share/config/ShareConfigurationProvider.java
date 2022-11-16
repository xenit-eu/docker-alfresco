package eu.xenit.alfresco.tomcat.embedded.share.config;

public interface ShareConfigurationProvider {

    default ShareConfiguration getConfiguration() {
        return getConfiguration(new ShareConfiguration());
    }

    ShareConfiguration getConfiguration(ShareConfiguration baseShareConfiguration);
}
