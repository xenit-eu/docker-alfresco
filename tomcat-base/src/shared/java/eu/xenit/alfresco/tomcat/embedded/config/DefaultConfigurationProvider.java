package eu.xenit.alfresco.tomcat.embedded.config;

public class DefaultConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        baseConfiguration.setWebappsPath("/usr/local/tomcat/webapps");
        baseConfiguration.setLogsLocation("/usr/local/tomcat/logs");
        baseConfiguration.setPort(8080);
        baseConfiguration.setEnableJsonLogging(false);
        baseConfiguration.setLogLibraryDir("/loglibrarydir");
        baseConfiguration.setTomcatMaxThreads(200);
        baseConfiguration.setTomcatMaxHttpHeaderSize(32768);
        baseConfiguration.setTomcatSslPort(8443);
        baseConfiguration.setTomcatServerPort(8005);

        baseConfiguration.setGlobalProperty("db.host", "postgresql");
        baseConfiguration.setGlobalProperty("db.port", "5432");
        baseConfiguration.setGlobalProperty("db.driver", "org.postgresql.Driver");
        baseConfiguration.setGlobalProperty("db.user", "alfresco");
        baseConfiguration.setGlobalProperty("db.password", "admin");
        baseConfiguration.setGlobalProperty("db.url", "jdbc:postgresql://${db.host}:${db.port}/${db.name}");
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
