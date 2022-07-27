package eu.xenit.alfresco.tomcat.embedded.config;

import java.util.function.Consumer;

public class EnvironmentVariableConfigurationProvider implements ConfigurationProvider {

    public static final String TOMCAT_WEBAPPS = "TOMCAT_WEBAPPS";
    public static final String JSON_LOGGING = "JSON_LOGGING";

    public static final String ACCESS_LOGGING = "ACCESS_LOGGING";

    private static final String LOGLIBRARY_DIR = "LOGLIBRARY_DIR";
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

    private static final String SOLR_SSL = "SOLR_SSL";

    private static final String SOLR_HOST = "SOLR_HOST";

    private static final String SOLR_PORT = "SOLR_PORT";

    private static final String SOLR_PORT_SSL = "SOLR_PORT_SSL";

    private static final String TOMCAT_SSL_KEYSTORE = "TOMCAT_SSL_KEYSTORE";
    private static final String TOMCAT_SSL_KEYSTORE_PASSWORD = "TOMCAT_SSL_KEYSTORE_PASSWORD";
    private static final String TOMCAT_SSL_TRUSTSTORE = "TOMCAT_SSL_TRUSTSTORE";
    private static final String TOMCAT_SSL_TRUSTSTORE_PASSWORD = "TOMCAT_SSL_TRUSTSTORE_PASSWORD";

    private static final String TOMCAT_PORT = "TOMCAT_PORT";

    private static final String TOMCAT_PORT_SSL = "TOMCAT_PORT_SSL";

    private static final String TOMCAT_SERVER_PORT = "TOMCAT_SERVER_PORT";

    private static final String TOMCAT_MAX_HTTP_HEADER_SIZE = "TOMCAT_MAX_HTTP_HEADER_SIZE";

    private static final String TOMCAT_MAX_THREADS = "TOMCAT_MAX_THREADS";

    private static final String TOMCAT_RELAXED_QUERY_CHARS = "TOMCAT_RELAXED_QUERY_CHARS";

    private static final String TOMCAT_RELAXED_PATH_CHARS = "TOMCAT_RELAXED_PATH_CHARS";

    private static final String ENABLE_CLUSTERING = "ENABLE_CLUSTERING";

    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        setPropertyFromEnv(TOMCAT_WEBAPPS, baseConfiguration::setWebappsPath);
        setPropertyFromEnv(JSON_LOGGING, value -> baseConfiguration.setJsonLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(ACCESS_LOGGING, value -> baseConfiguration.setAccessLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(LOGLIBRARY_DIR, baseConfiguration::setLogLibraryDir);
        setPropertyFromEnv(ALFRESCO_VERSION, baseConfiguration::setAlfrescoVersion);
        setPropertyFromEnv(ALFRESCO_FLAVOUR, baseConfiguration::setAlfrescoFlavour);
        setGlobalPropertyFromEnv(DB_HOST, baseConfiguration, "db.host");
        setGlobalPropertyFromEnv(DB_NAME, baseConfiguration, "db.NAME");
        setGlobalPropertyFromEnv(DB_PORT, baseConfiguration, "db.port");
        setGlobalPropertyFromEnv(DB_DRIVER, baseConfiguration, "db.driver");
        setGlobalPropertyFromEnv(DB_USERNAME, baseConfiguration, "db.username");
        setGlobalPropertyFromEnv(DB_PASSWORD, baseConfiguration, "db.password");
        setGlobalPropertyFromEnv(DB_URL, baseConfiguration, "db.url");
        setGlobalPropertyFromEnv(DB_QUERY, baseConfiguration, "db.query");
        setGlobalPropertyFromEnv(SOLR_HOST, baseConfiguration, "solr.host");
        setGlobalPropertyFromEnv(SOLR_PORT, baseConfiguration, "solr.port");
        setGlobalPropertyFromEnv(SOLR_PORT_SSL, baseConfiguration, "solr.port.ssl");
        if ("none".equals(System.getenv(SOLR_SSL))) {
            baseConfiguration.setSolrSSLEnabled(false);
        }
        setGlobalPropertyFromEnv(SOLR_SSL, baseConfiguration, "solr.secureComms");
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE, baseConfiguration::setTomcatSSLKeystore);
        setPropertyFromEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, baseConfiguration::setTomcatSSLKeystorePassword);
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE, baseConfiguration::setTomcatSSLTruststore);
        setPropertyFromEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, baseConfiguration::setTomcatSSLTruststorePassword);
        setGlobalPropertyFromEnv(ENABLE_CLUSTERING, baseConfiguration, "alfresco.cluster.enabled");
        setPropertyFromEnv(TOMCAT_PORT, value -> baseConfiguration.setPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_PORT_SSL, value -> baseConfiguration.setTomcatSslPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_SERVER_PORT, value -> baseConfiguration.setTomcatServerPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_MAX_HTTP_HEADER_SIZE, value -> baseConfiguration.setTomcatMaxHttpHeaderSize(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_MAX_THREADS, value -> baseConfiguration.setTomcatMaxThreads(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_RELAXED_QUERY_CHARS, baseConfiguration::setTomcatRelaxedQueryChars);
        setPropertyFromEnv(TOMCAT_RELAXED_PATH_CHARS, baseConfiguration::setTomcatRelaxedPathChars);

        setGlobalProperties(baseConfiguration);

        return baseConfiguration;
    }

    private void setGlobalProperties(Configuration baseConfiguration) {
        System.getenv().forEach((key, value) -> {
            if (key.startsWith("GLOBAL_")) {
                String prop = key.substring(7);
                baseConfiguration.setGlobalProperty(prop, value);
            }
        });
    }

    private void setPropertyFromEnv(String env, Consumer<String> stringConsumer) {
        if (System.getenv(env) != null) {
            stringConsumer.accept(System.getenv(env));
        }
    }

    private void setGlobalPropertyFromEnv(String env, Configuration configuration, String property) {
        setPropertyFromEnv(env, value -> configuration.setGlobalProperty(property, value));
    }
}
