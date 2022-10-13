package eu.xenit.alfresco.tomcat.embedded.config;

public class DefaultConfigurationProvider implements ConfigurationProvider {

    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        baseConfiguration.setWebappsPath("/usr/local/tomcat/webapps");
        baseConfiguration.setLogsLocation("/usr/local/tomcat/logs");
        baseConfiguration.setPort(8080);
        baseConfiguration.setJsonLogging(false);
        baseConfiguration.setAccessLogging(false);
        baseConfiguration.setLogLibraryDir("/loglibrarydir");
        baseConfiguration.setTomcatMaxThreads(200);
        baseConfiguration.setTomcatMaxHttpHeaderSize(32768);
        baseConfiguration.setTomcatSslPort(8443);
        baseConfiguration.setTomcatServerPort(8005);
        baseConfiguration.setSolrSSLEnabled(true);
        baseConfiguration.setTomcatSSLKeystore("/keystore/ssl.keystore");
        baseConfiguration.setTomcatSSLTruststore("/keystore/ssl.truststore");
        baseConfiguration.setTomcatSSLKeystorePassword("kT9X6oe68t");
        baseConfiguration.setTomcatSSLTruststorePassword("kT9X6oe68t");

        baseConfiguration.setGlobalProperty("db.host", "postgresql");
        baseConfiguration.setGlobalProperty("db.port", "5432");
        baseConfiguration.setGlobalProperty("db.driver", "org.postgresql.Driver");
        baseConfiguration.setGlobalProperty("db.user", "alfresco");
        baseConfiguration.setGlobalProperty("db.password", "admin");
        baseConfiguration.setGlobalProperty("db.url", "jdbc:postgresql://${db.host}:${db.port}/${db.name}");

        baseConfiguration.setSystemProperty("encryption.keystore.type", "JCEKS");
        baseConfiguration.setSystemProperty("encryption.cipherAlgorithm", "DESede/CBC/PKCS5Padding");
        baseConfiguration.setSystemProperty("encryption.keyAlgorithm", "DESede");
        baseConfiguration.setSystemProperty("encryption.keystore.location", "/keystore/keystore");
        baseConfiguration.setSystemProperty("encryption.keystore.keyMetaData.location", "/keystore/keystore-passwords.properties");
        baseConfiguration.setSystemProperty("metadata-keystore.password", "mp6yc0UD9e");
        baseConfiguration.setSystemProperty("metadata-keystore.aliases", "metadata");
        baseConfiguration.setSystemProperty("metadata-keystore.metadata.password", "oKIWzVdEdA");
        baseConfiguration.setSystemProperty("metadata-keystore.metadata.algorithm", "DESede");

        baseConfiguration.setGlobalProperty("encryption.ssl.keystore.location", "/keystore/ssl.keystore");
        baseConfiguration.setGlobalProperty("encryption.ssl.keystore.keyMetaData.location","/keystore/ssl-keystore-passwords.properties");
        baseConfiguration.setGlobalProperty("encryption.ssl.keystore.type", "JCEKS");
        baseConfiguration.setGlobalProperty("encryption.ssl.truststore.location", "/keystore/ssl.truststore");
        baseConfiguration.setGlobalProperty("encryption.ssl.truststore.keyMetaData.location", "/keystore/ssl-truststore-passwords.properties");
        baseConfiguration.setGlobalProperty("encryption.ssl.truststore.type", "JCEKS");

        baseConfiguration.setSystemProperty("ssl-truststore.password", "kT9X6oe68t");
        baseConfiguration.setSystemProperty("ssl-keystore.password", "kT9X6oe68t");

        baseConfiguration.setGlobalProperty("messaging.subsystem.autoStart", "false");
        baseConfiguration.setGlobalProperty("events.subsystem.autoStart", "false");
        baseConfiguration.setGlobalProperty("local.transform.service.enabled", "true");

        baseConfiguration.setGlobalProperty("index.subsystem.name", "solr6");
        baseConfiguration.setGlobalProperty("solr.host", "solr");
        baseConfiguration.setGlobalProperty("solr.port", "8080");
        baseConfiguration.setGlobalProperty("solr.port.ssl", "8443");
        baseConfiguration.setGlobalProperty("solr.secureComms", "https");

        baseConfiguration.setGlobalProperty("dir.root", "/opt/alfresco/alf_data");

        baseConfiguration.setExitOnFailure(true);

        return baseConfiguration;
    }
}
