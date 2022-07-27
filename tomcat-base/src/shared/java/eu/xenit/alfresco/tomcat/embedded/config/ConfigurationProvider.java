package eu.xenit.alfresco.tomcat.embedded.config;

public interface ConfigurationProvider {

    public default Configuration getConfiguration() {
        return getConfiguration(new Configuration());
    }
    public Configuration getConfiguration(Configuration baseConfiguration);
}
