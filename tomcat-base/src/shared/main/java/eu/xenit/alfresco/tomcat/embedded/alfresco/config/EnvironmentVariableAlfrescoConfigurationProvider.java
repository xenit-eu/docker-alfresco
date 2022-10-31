package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.ALFRESCO_FLAVOUR;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.ALFRESCO_VERSION;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_DRIVER;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_HOST;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_NAME;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_PASSWORD;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_PORT;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_QUERY;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_URL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DB_USERNAME;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.DIR_ROOT;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.ENABLE_CLUSTERING;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.INDEX;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_HOST;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_PORT;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_PORT_SSL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.SOLR_SSL;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_KEYSTORE;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_KEYSTORE_PASSWORD;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_TRUSTSTORE;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_TRUSTSTORE_PASSWORD;
import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

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
        setGlobalPropertyFromEnv(DB_NAME, finalBaseAlfrescoConfiguration, "db.NAME");
        setGlobalPropertyFromEnv(DB_PORT, finalBaseAlfrescoConfiguration, "db.port");
        setGlobalPropertyFromEnv(DB_DRIVER, finalBaseAlfrescoConfiguration, "db.driver");
        setGlobalPropertyFromEnv(DB_USERNAME, finalBaseAlfrescoConfiguration, "db.username");
        setGlobalPropertyFromEnv(DB_PASSWORD, finalBaseAlfrescoConfiguration, "db.password");
        setGlobalPropertyFromEnv(DB_URL, finalBaseAlfrescoConfiguration, "db.url");
        setGlobalPropertyFromEnv(DB_QUERY, finalBaseAlfrescoConfiguration, "db.query");
        setGlobalPropertyFromEnv(SOLR_HOST, finalBaseAlfrescoConfiguration, "solr.host");
        setGlobalPropertyFromEnv(SOLR_PORT, finalBaseAlfrescoConfiguration, "solr.port");
        setGlobalPropertyFromEnv(SOLR_PORT_SSL, finalBaseAlfrescoConfiguration, "solr.port.ssl");
        if ("none".equals(System.getenv(SOLR_SSL))) {
            finalBaseAlfrescoConfiguration.setSolrSSLEnabled(false);
        }
        setGlobalPropertyFromEnv(SOLR_SSL, finalBaseAlfrescoConfiguration, "solr.secureComms");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.keyMetaData.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, finalBaseAlfrescoConfiguration, "ssl-keystore.password");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.keyMetaData.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, finalBaseAlfrescoConfiguration, "ssl-truststore.password");
        setGlobalPropertyFromEnv(ENABLE_CLUSTERING, finalBaseAlfrescoConfiguration, "alfresco.cluster.enabled");
        setGlobalPropertyFromEnv(INDEX, finalBaseAlfrescoConfiguration, "index.subsystem.name");
        setGlobalPropertyFromEnv(DIR_ROOT, finalBaseAlfrescoConfiguration, "dir.root");
        setGlobalProperties(finalBaseAlfrescoConfiguration);

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
