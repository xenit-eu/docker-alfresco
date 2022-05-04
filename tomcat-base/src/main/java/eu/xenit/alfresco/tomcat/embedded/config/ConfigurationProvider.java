package eu.xenit.alfresco.tomcat.embedded.config;

public interface ConfigurationProvider {

    default public Configuration getConfiguration() {
        return getConfiguration(new Configuration());
    }
    public Configuration getConfiguration(Configuration baseConfiguration);
}
