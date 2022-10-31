package eu.xenit.alfresco.tomcat.embedded.alfresco.config;



public class DefaultAlfrescoConfigurationProvider implements AlfrescoConfigurationProvider {


    public AlfrescoConfiguration getConfiguration(AlfrescoConfiguration baseAlfrescoConfiguration) {
        baseAlfrescoConfiguration.setTomcatSSLKeystore("/keystore/ssl.keystore");
        baseAlfrescoConfiguration.setTomcatSSLTruststore("/keystore/ssl.truststore");
        baseAlfrescoConfiguration.setTomcatSSLKeystorePassword("kT9X6oe68t");
        baseAlfrescoConfiguration.setTomcatSSLTruststorePassword("kT9X6oe68t");
        baseAlfrescoConfiguration.setSolrSSLEnabled(true);
        baseAlfrescoConfiguration.setGlobalProperty("db.host", "postgresql");
        baseAlfrescoConfiguration.setGlobalProperty("db.port", "5432");
        baseAlfrescoConfiguration.setGlobalProperty("db.driver", "org.postgresql.Driver");
        baseAlfrescoConfiguration.setGlobalProperty("db.user", "alfresco");
        baseAlfrescoConfiguration.setGlobalProperty("db.password", "admin");
        baseAlfrescoConfiguration.setGlobalProperty("db.url", "jdbc:postgresql://${db.host}:${db.port}/${db.name}");

        baseAlfrescoConfiguration.setSystemProperty("encryption.keystore.type", "JCEKS");
        baseAlfrescoConfiguration.setSystemProperty("encryption.cipherAlgorithm", "DESede/CBC/PKCS5Padding");
        baseAlfrescoConfiguration.setSystemProperty("encryption.keyAlgorithm", "DESede");
        baseAlfrescoConfiguration.setSystemProperty("encryption.keystore.location", "/keystore/keystore");
        baseAlfrescoConfiguration.setSystemProperty("encryption.keystore.keyMetaData.location", "/keystore/keystore-passwords.properties");
        baseAlfrescoConfiguration.setSystemProperty("metadata-keystore.password", "mp6yc0UD9e");
        baseAlfrescoConfiguration.setSystemProperty("metadata-keystore.aliases", "metadata");
        baseAlfrescoConfiguration.setSystemProperty("metadata-keystore.metadata.password", "oKIWzVdEdA");
        baseAlfrescoConfiguration.setSystemProperty("metadata-keystore.metadata.algorithm", "DESede");

        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.keystore.location", "/keystore/ssl.keystore");
        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.keystore.keyMetaData.location", "/keystore/ssl-keystore-passwords.properties");
        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.keystore.type", "JCEKS");
        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.truststore.location", "/keystore/ssl.truststore");
        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.truststore.keyMetaData.location", "/keystore/ssl-truststore-passwords.properties");
        baseAlfrescoConfiguration.setGlobalProperty("encryption.ssl.truststore.type", "JCEKS");

        baseAlfrescoConfiguration.setSystemProperty("ssl-truststore.password", "kT9X6oe68t");
        baseAlfrescoConfiguration.setSystemProperty("ssl-keystore.password", "kT9X6oe68t");

        baseAlfrescoConfiguration.setGlobalProperty("messaging.subsystem.autoStart", "false");
        baseAlfrescoConfiguration.setGlobalProperty("events.subsystem.autoStart", "false");
        baseAlfrescoConfiguration.setGlobalProperty("local.transform.service.enabled", "true");

        baseAlfrescoConfiguration.setGlobalProperty("index.subsystem.name", "solr6");
        baseAlfrescoConfiguration.setGlobalProperty("solr.host", "solr");
        baseAlfrescoConfiguration.setGlobalProperty("solr.port", "8080");
        baseAlfrescoConfiguration.setGlobalProperty("solr.port.ssl", "8443");
        baseAlfrescoConfiguration.setGlobalProperty("solr.secureComms", "https");

        baseAlfrescoConfiguration.setGlobalProperty("dir.root", "/opt/alfresco/alf_data");

        return baseAlfrescoConfiguration;
    }
}
