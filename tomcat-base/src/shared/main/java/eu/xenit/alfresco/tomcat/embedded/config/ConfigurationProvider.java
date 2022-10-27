package eu.xenit.alfresco.tomcat.embedded.config;


public interface ConfigurationProvider {

     default Configuration getConfiguration() {
        return getConfiguration(new Configuration());
    }

     Configuration getConfiguration(Configuration baseConfiguration);
}
