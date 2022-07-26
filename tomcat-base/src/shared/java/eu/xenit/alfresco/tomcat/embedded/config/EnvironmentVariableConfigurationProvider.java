package eu.xenit.alfresco.tomcat.embedded.config;

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
        if (System.getenv(TOMCAT_WEBAPPS) != null) {
            baseConfiguration.setWebappsPath(System.getenv(TOMCAT_WEBAPPS));
        }
        if (System.getenv(JSON_LOGGING) != null) {
            baseConfiguration.setJsonLogging(Boolean.parseBoolean(System.getenv(JSON_LOGGING)));
        }
        if (System.getenv(ACCESS_LOGGING) != null) {
            baseConfiguration.setAccessLogging(Boolean.parseBoolean(System.getenv(ACCESS_LOGGING)));
        }
        if (System.getenv(LOGLIBRARY_DIR) != null) {
            baseConfiguration.setLogLibraryDir(System.getenv(LOGLIBRARY_DIR));
        }
        if (System.getenv(ALFRESCO_VERSION) != null) {
            baseConfiguration.setAlfrescoVersion(System.getenv(ALFRESCO_VERSION));
        }
        if (System.getenv(ALFRESCO_FLAVOUR) != null) {
            baseConfiguration.setAlfrescoFlavour(System.getenv(ALFRESCO_FLAVOUR));
        }
        if (System.getenv(DB_HOST) != null) {
            baseConfiguration.setGlobalProperty("db.host", System.getenv(DB_HOST));
        }
        if (System.getenv(DB_PORT) != null) {
            baseConfiguration.setGlobalProperty("db.port", System.getenv(DB_PORT));
        }
        if (System.getenv(DB_NAME) != null) {
            baseConfiguration.setGlobalProperty("db.name", System.getenv(DB_NAME));
        }
        if (System.getenv(DB_DRIVER) != null) {
            baseConfiguration.setGlobalProperty("db.driver", System.getenv(DB_DRIVER));
        }
        if (System.getenv(DB_USERNAME) != null) {
            baseConfiguration.setGlobalProperty("db.username", System.getenv(DB_USERNAME));
        }
        if (System.getenv(DB_PASSWORD) != null) {
            baseConfiguration.setGlobalProperty("db.password", System.getenv(DB_PASSWORD));
        }
        if (System.getenv(DB_URL) != null) {
            baseConfiguration.setGlobalProperty("db.url", System.getenv(DB_URL));
        }
        if (System.getenv(DB_QUERY) != null) {
            baseConfiguration.setGlobalProperty("db.query", System.getenv(DB_QUERY));
        }
        if (System.getenv(SOLR_HOST) != null) {
            baseConfiguration.setGlobalProperty("solr.host", System.getenv(SOLR_HOST));
        }
        if (System.getenv(SOLR_PORT) != null) {
            baseConfiguration.setGlobalProperty("solr.port", System.getenv(SOLR_PORT));
        }
        if (System.getenv(SOLR_PORT_SSL) != null) {
            baseConfiguration.setGlobalProperty("solr.port.ssl", System.getenv(SOLR_PORT_SSL));
        }
        if ("none".equals(System.getenv(SOLR_SSL))) {
            baseConfiguration.setSolrSSLEnabled(false);
        }
        if (System.getenv(SOLR_SSL) != null) {
            baseConfiguration.setGlobalProperty("solr.secureComms", System.getenv(SOLR_SSL));
        }
        if (System.getenv(TOMCAT_SSL_KEYSTORE) != null) {
            baseConfiguration.setTomcatSSLKeystore(System.getenv(TOMCAT_SSL_KEYSTORE));
        }
        if (System.getenv(TOMCAT_SSL_KEYSTORE_PASSWORD) != null) {
            baseConfiguration.setTomcatSSLKeystorePassword(System.getenv(TOMCAT_SSL_KEYSTORE_PASSWORD));
        }
        if (System.getenv(TOMCAT_SSL_TRUSTSTORE) != null) {
            baseConfiguration.setTomcatSSLTruststore(System.getenv(TOMCAT_SSL_TRUSTSTORE));
        }
        if (System.getenv(TOMCAT_SSL_TRUSTSTORE_PASSWORD) != null) {
            baseConfiguration.setTomcatSSLTruststorePassword(System.getenv(TOMCAT_SSL_TRUSTSTORE_PASSWORD));
        }
        if (System.getenv(ENABLE_CLUSTERING) != null) {
            baseConfiguration.setGlobalProperty("alfresco.cluster.enabled", System.getenv(ENABLE_CLUSTERING));
        }
        if (System.getenv(TOMCAT_PORT) != null) {
            baseConfiguration.setPort(Integer.parseInt(System.getenv(TOMCAT_PORT)));
        }
        if (System.getenv(TOMCAT_PORT_SSL) != null) {
            baseConfiguration.setTomcatSslPort(Integer.parseInt(System.getenv(TOMCAT_PORT_SSL)));
        }
        if (System.getenv(TOMCAT_SERVER_PORT) != null) {
            baseConfiguration.setTomcatServerPort(Integer.parseInt(System.getenv(TOMCAT_SERVER_PORT)));
        }
        if (System.getenv(TOMCAT_MAX_HTTP_HEADER_SIZE) != null) {
            baseConfiguration.setTomcatMaxHttpHeaderSize(Integer.parseInt(System.getenv(TOMCAT_MAX_HTTP_HEADER_SIZE)));
        }
        if (System.getenv(TOMCAT_MAX_THREADS) != null) {
            baseConfiguration.setTomcatMaxThreads(Integer.parseInt(System.getenv(TOMCAT_MAX_THREADS)));
        }
        if (System.getenv(TOMCAT_RELAXED_QUERY_CHARS) != null) {
            baseConfiguration.setTomcatRelaxedQueryChars(System.getenv(TOMCAT_RELAXED_QUERY_CHARS));
        }
        if (System.getenv(TOMCAT_RELAXED_PATH_CHARS) != null) {
            baseConfiguration.setTomcatRelaxedPathChars(System.getenv(TOMCAT_RELAXED_PATH_CHARS));
        }

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
}
