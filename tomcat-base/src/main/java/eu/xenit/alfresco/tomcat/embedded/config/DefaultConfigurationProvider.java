package eu.xenit.alfresco.tomcat.embedded.config;

public class DefaultConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        baseConfiguration.setWebappsPath("/usr/local/tomcat/webapps");
        baseConfiguration.setLogsLocation("/usr/local/tomcat/logs");
        baseConfiguration.setPort(8080);
        baseConfiguration.setEnableJsonLogging(true);
        baseConfiguration.setLogLibraryDir("/loglibrarydir");

        baseConfiguration.setGlobalProperty("db.driver", "org.postgresql.Driver");
        baseConfiguration.setGlobalProperty("db.user", "alfresco");
        baseConfiguration.setGlobalProperty("db.password", "admin");
        baseConfiguration.setGlobalProperty("encryption.keystore.type", "JCEKS");
        baseConfiguration.setGlobalProperty("encryption.cipherAlgorithm", "DESede/CBC/PKCS5Padding");
        baseConfiguration.setGlobalProperty("encryption.keyAlgorithm", "DESede");
        baseConfiguration.setGlobalProperty("encryption.keystore.location", "file:/keystore/keystore");
        baseConfiguration.setGlobalProperty("metadata-keystore.password", "mp6yc0UD9e");
        baseConfiguration.setGlobalProperty("metadata-keystore.aliases", "metadata");
        baseConfiguration.setGlobalProperty("metadata-keystore.metadata.password", "oKIWzVdEdA");
        baseConfiguration.setGlobalProperty("metadata-keystore.metadata.algorithm", "DESede");
        baseConfiguration.setGlobalProperty("messaging.subsystem.autoStart", "false");
        baseConfiguration.setGlobalProperty("events.subsystem.autoStart", "false");
        baseConfiguration.setGlobalProperty("local.transform.service.enabled", "false");

        return baseConfiguration;
    }
}
