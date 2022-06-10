package eu.xenit.alfresco.tomcat.embedded.config;

public class EnvironmentVariableConfigurationProvider implements ConfigurationProvider {

    public static final String TOMCAT_WEBAPPS = "TOMCAT_WEBAPPS";
    public static final String JSON_LOGGING = "JSON_LOGGING";
    private static final String LOGLIBRARY_DIR = "LOGLIBRARY_DIR";
    private static final String ALFRESCO_VERSION = "ALFRESCO_VERSION";
    private static final String ALFRESCO_FLAVOUR = "ALFRESCO_FLAVOUR";
    private static final String DB_HOST = "DB_HOST";
    private static final String DB_PORT = "DB_PORT";
    private static final String DB_NAME = "DB_NAME";

    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        if (System.getenv(TOMCAT_WEBAPPS) != null) {
            baseConfiguration.setWebappsPath(System.getenv(TOMCAT_WEBAPPS));
        }
        if (System.getenv(JSON_LOGGING) != null) {
            baseConfiguration.setEnableJsonLogging(Boolean.parseBoolean(System.getenv(JSON_LOGGING)));
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
