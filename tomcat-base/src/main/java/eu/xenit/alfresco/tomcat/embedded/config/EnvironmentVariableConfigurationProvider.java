package eu.xenit.alfresco.tomcat.embedded.config;

public class EnvironmentVariableConfigurationProvider implements ConfigurationProvider {

    public static final String TOMCAT_WEBAPPS = "TOMCAT_WEBAPPS";
    public static final String JSON_LOGGING = "JSON_LOGGING";
    private static final String LOGLIBRARY_DIR = "LOGLIBRARY_DIR";
    private static final String ALFRESCO_VERSION = "ALFRESCO_VERSION";
    private static final String ALFRESCO_FLAVOUR = "ALFRESCO_FLAVOUR";

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
