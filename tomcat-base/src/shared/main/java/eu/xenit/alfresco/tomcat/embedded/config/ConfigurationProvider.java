package eu.xenit.alfresco.tomcat.embedded.config;


public interface ConfigurationProvider {

    default TomcatConfiguration getConfiguration() {
        return getConfiguration(new TomcatConfiguration());
    }

    TomcatConfiguration getConfiguration(TomcatConfiguration baseConfiguration);
}
