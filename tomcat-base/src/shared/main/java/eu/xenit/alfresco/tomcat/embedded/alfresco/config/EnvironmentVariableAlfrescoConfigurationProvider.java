package eu.xenit.alfresco.tomcat.embedded.alfresco.config;


import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariableConfigurationProvider;

import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

public class EnvironmentVariableAlfrescoConfigurationProvider implements AlfrescoConfigurationProvider {

    private static final String ALFRESCO_VERSION = "ALFRESCO_VERSION";
    private static final String ALFRESCO_FLAVOUR = "ALFRESCO_FLAVOUR";
    private static final String DB_HOST = "DB_HOST";
    private static final String DB_PORT = "DB_PORT";
    private static final String DB_NAME = "DB_NAME";

    private static final String DB_DRIVER = "DB_DRIVER";

    private static final String DB_USERNAME = "DB_USERNAME";

    private static final String DB_PASSWORD = "DB_PASSWORD";

    private static final String DB_URL = "DB_URL";

    private static final String DB_QUERY = "DB_QUERY";

    private static final String INDEX = "INDEX";

    private static final String SOLR_SSL = "SOLR_SSL";

    private static final String SOLR_HOST = "SOLR_HOST";

    private static final String SOLR_PORT = "SOLR_PORT";

    private static final String SOLR_PORT_SSL = "SOLR_PORT_SSL";

    private static final String TOMCAT_SSL_KEYSTORE = "TOMCAT_SSL_KEYSTORE";
    private static final String TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION = "TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION";
    private static final String TOMCAT_SSL_KEYSTORE_PASSWORD = "TOMCAT_SSL_KEYSTORE_PASSWORD";
    private static final String TOMCAT_SSL_TRUSTSTORE = "TOMCAT_SSL_TRUSTSTORE";
    private static final String TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION = "TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION";
    private static final String TOMCAT_SSL_TRUSTSTORE_PASSWORD = "TOMCAT_SSL_TRUSTSTORE_PASSWORD";
    private static final String ENABLE_CLUSTERING = "ENABLE_CLUSTERING";

    private static final String DIR_ROOT = "DIR_ROOT";

    @Override
    public AlfrescoConfiguration getConfiguration(Configuration baseConfiguration) {
        AlfrescoConfiguration finalBaseAlfrescoConfiguration = (AlfrescoConfiguration) baseConfiguration;

        setPropertyFromEnv(ALFRESCO_VERSION, finalBaseAlfrescoConfiguration::setAlfrescoVersion);
        setPropertyFromEnv(ALFRESCO_FLAVOUR, finalBaseAlfrescoConfiguration::setAlfrescoFlavour);
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
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE, finalBaseAlfrescoConfiguration::setTomcatSSLKeystore);
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.keystore.keyMetaData.location");
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, finalBaseAlfrescoConfiguration::setTomcatSSLKeystorePassword);
        setGlobalPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, finalBaseAlfrescoConfiguration, "ssl-keystore.password");
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, finalBaseAlfrescoConfiguration::setTomcatSSLTruststore);
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.location");
        setGlobalPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION, finalBaseAlfrescoConfiguration, "encryption.ssl.truststore.keyMetaData.location");
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, finalBaseAlfrescoConfiguration::setTomcatSSLTruststorePassword);
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
        setPropertyFromEnv(env, value -> alfrescoConfiguration.setGlobalProperty(property, value));
    }
}
