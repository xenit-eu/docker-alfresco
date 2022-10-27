package eu.xenit.alfresco.tomcat.embedded.config;


import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

public class EnvironmentVariableConfigurationProvider implements ConfigurationProvider {

    public static final String TOMCAT_WEBAPPS = "TOMCAT_WEBAPPS";
    public static final String JSON_LOGGING = "JSON_LOGGING";

    public static final String ACCESS_LOGGING = "ACCESS_LOGGING";

    private static final String LOGLIBRARY_DIR = "LOGLIBRARY_DIR";

    private static final String TOMCAT_PORT = "TOMCAT_PORT";

    private static final String TOMCAT_PORT_SSL = "TOMCAT_PORT_SSL";

    private static final String TOMCAT_SERVER_PORT = "TOMCAT_SERVER_PORT";

    private static final String TOMCAT_MAX_HTTP_HEADER_SIZE = "TOMCAT_MAX_HTTP_HEADER_SIZE";

    private static final String TOMCAT_MAX_THREADS = "TOMCAT_MAX_THREADS";

    private static final String TOMCAT_RELAXED_QUERY_CHARS = "TOMCAT_RELAXED_QUERY_CHARS";

    private static final String TOMCAT_RELAXED_PATH_CHARS = "TOMCAT_RELAXED_PATH_CHARS";

    private static final String EXIT_ON_FAILURE = "EXIT_ON_FAILURE";
    private static final String ALFRESCO_ENABLED = "ALFRESCO_ENABLED";
    private static final String SHARE_ENABLED = "SHARE_ENABLED";


    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        setPropertyFromEnv(TOMCAT_WEBAPPS, baseConfiguration::setWebappsPath);
        setPropertyFromEnv(JSON_LOGGING, value -> baseConfiguration.setJsonLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(ACCESS_LOGGING, value -> baseConfiguration.setAccessLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(LOGLIBRARY_DIR, baseConfiguration::setSharedClassesPath);
        setPropertyFromEnv(TOMCAT_PORT, value -> baseConfiguration.setPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_PORT_SSL, value -> baseConfiguration.setTomcatSslPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_SERVER_PORT, value -> baseConfiguration.setTomcatServerPort(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_MAX_HTTP_HEADER_SIZE, value -> baseConfiguration.setTomcatMaxHttpHeaderSize(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_MAX_THREADS, value -> baseConfiguration.setTomcatMaxThreads(Integer.parseInt(value)));
        setPropertyFromEnv(TOMCAT_RELAXED_QUERY_CHARS, baseConfiguration::setTomcatRelaxedQueryChars);
        setPropertyFromEnv(TOMCAT_RELAXED_PATH_CHARS, baseConfiguration::setTomcatRelaxedPathChars);
        setPropertyFromEnv(EXIT_ON_FAILURE, value -> baseConfiguration.setExitOnFailure(Boolean.parseBoolean(value)));
        setPropertyFromEnv(ALFRESCO_ENABLED, value -> baseConfiguration.setAlfrescoEnabled(Boolean.parseBoolean(value)));
        setPropertyFromEnv(SHARE_ENABLED, value -> baseConfiguration.setShareEnabled(Boolean.parseBoolean(value)));
        return baseConfiguration;
    }
}
