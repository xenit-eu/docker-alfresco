package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.*;
import static eu.xenit.alfresco.tomcat.embedded.utils.Utils.setPropertyFromEnv;

public class EnvironmentVariableAlfrescoConfigurationProvider implements AlfrescoConfigurationProvider {


    @Override
    public AlfrescoConfiguration getConfiguration(AlfrescoConfiguration finalBaseAlfrescoConfiguration) {
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, finalBaseAlfrescoConfiguration::setTomcatSSLKeystorePassword);
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, finalBaseAlfrescoConfiguration::setTomcatSSLTruststore);
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, finalBaseAlfrescoConfiguration::setTomcatSSLTruststorePassword);
        setPropertyFromEnv(ALFRESCO_VERSION, finalBaseAlfrescoConfiguration::setAlfrescoVersion);
        setPropertyFromEnv(ALFRESCO_FLAVOUR, finalBaseAlfrescoConfiguration::setAlfrescoFlavour);
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE, finalBaseAlfrescoConfiguration::setTomcatSSLKeystore);

        setGlobalPropertyFromEnv(DB_HOST, finalBaseAlfrescoConfiguration, "db.host");
        setGlobalPropertyFromEnv(DB_NAME, finalBaseAlfrescoConfiguration, "db.name");
        setGlobalPropertyFromEnv(DB_PORT, finalBaseAlfrescoConfiguration, "db.port");
        setGlobalPropertyFromEnv(DB_DRIVER, finalBaseAlfrescoConfiguration, "db.driver");
        setGlobalPropertyFromEnv(DB_USERNAME, finalBaseAlfrescoConfiguration, "db.username");
        setGlobalPropertyFromEnv(DB_PASSWORD, finalBaseAlfrescoConfiguration, "db.password");
        setGlobalPropertyFromEnv(DB_URL, finalBaseAlfrescoConfiguration, "db.url");
        setGlobalPropertyFromEnv(DB_QUERY, finalBaseAlfrescoConfiguration, "db.query");
        setGlobalPropertyFromEnv(SOLR_HOST, finalBaseAlfrescoConfiguration, "solr.host");
        setGlobalPropertyFromEnv(SOLR_PORT, finalBaseAlfrescoConfiguration, "solr.port");
        setGlobalPropertyFromEnv(SOLR_PORT_SSL, finalBaseAlfrescoConfiguration, "solr.port.ssl");
        setGlobalPropertyFromEnv(SOLR_SSL, finalBaseAlfrescoConfiguration, "solr.secureComms");
        setGlobalPropertyFromEnv(ELASTICSEARCH_HOST, finalBaseAlfrescoConfiguration, "elasticsearch.host");
        setGlobalPropertyFromEnv(ELASTICSEARCH_PORT, finalBaseAlfrescoConfiguration, "elasticsearch.port");
        setGlobalPropertyFromEnv(ELASTICSEARCH_SECURE_COMMS, finalBaseAlfrescoConfiguration, "elasticsearch.secureComms");
        setGlobalPropertyFromEnv(ELASTICSEARCH_CREATE_INDEX_IF_NOT_EXISTS, finalBaseAlfrescoConfiguration, "elasticsearch.createIndexIfNotExists");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.keyMetaData.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, finalBaseAlfrescoConfiguration, "ssl-keystore.password");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.keyMetaData.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, finalBaseAlfrescoConfiguration, "ssl-truststore.password");
        setGlobalPropertyFromEnv(ENABLE_CLUSTERING, finalBaseAlfrescoConfiguration, "alfresco.cluster.enabled");
        setGlobalPropertyFromEnv(INDEX, finalBaseAlfrescoConfiguration, "index.subsystem.name");
        setGlobalPropertyFromEnv(DIR_ROOT, finalBaseAlfrescoConfiguration, "dir.root");
        setGlobalPropertyFromEnv(ALFRESCO_HOST, finalBaseAlfrescoConfiguration, "alfresco.host");
        setGlobalPropertyFromEnv(ALFRESCO_PORT, finalBaseAlfrescoConfiguration, "alfresco.port");
        setGlobalPropertyFromEnv(ALFRESCO_PROTOCOL, finalBaseAlfrescoConfiguration, "alfresco.protocol");
        setGlobalPropertyFromEnv(SHARE_HOST, finalBaseAlfrescoConfiguration, "share.host");
        setGlobalPropertyFromEnv(SHARE_PORT, finalBaseAlfrescoConfiguration, "share.port");
        setGlobalPropertyFromEnv(SHARE_PROTOCOL, finalBaseAlfrescoConfiguration, "share.protocol");
        setGlobalProperties(finalBaseAlfrescoConfiguration);

        finalBaseAlfrescoConfiguration.setSolrSSLEnabled(
            "https".equals(finalBaseAlfrescoConfiguration.getGlobalProperties().get("solr.secureComms"))
        );
        return finalBaseAlfrescoConfiguration;
    }

    private void setGlobalProperties(AlfrescoConfiguration baseAlfrescoConfiguration) {
        System.getenv().forEach((key, value) -> {
            if (key.startsWith("GLOBAL_")) {
                String prop = key.substring(7);
                baseAlfrescoConfiguration.setGlobalProperty(prop, value);
            }
        });
    }


    private void setGlobalPropertyFromEnv(String env, AlfrescoConfiguration alfrescoConfiguration, String property) {
        setPropertyFromEnv(env, value ->
                alfrescoConfiguration.setGlobalProperty(property, value));
    }
}
