package eu.xenit.alfresco.tomcat.embedded.config;


import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.ACCESS_LOGGING;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.ALFRESCO_ENABLED;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.GENERATED_CLASSPATH_DIR;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.EXIT_ON_FAILURE;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.JSON_LOGGING;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.SHARED_CLASSPATH_DIR;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.SHARED_LIBS_DIR;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.SHARE_ENABLED;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_BASE_DIR;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_MAX_HTTP_HEADER_SIZE;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_MAX_THREADS;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_PORT;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_PORT_SSL;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_RELAXED_PATH_CHARS;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_RELAXED_QUERY_CHARS;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_SERVER_PORT;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.TOMCAT_WEBAPPS;
import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

public class EnvironmentVariableConfigurationProvider implements ConfigurationProvider {


    @Override
    public Configuration getConfiguration(Configuration baseConfiguration) {
        setPropertyFromEnv(TOMCAT_WEBAPPS, baseConfiguration::setWebappsPath);
        setPropertyFromEnv(JSON_LOGGING, value -> baseConfiguration.setJsonLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(ACCESS_LOGGING, value -> baseConfiguration.setAccessLogging(Boolean.parseBoolean(value)));
        setPropertyFromEnv(SHARED_LIBS_DIR, baseConfiguration::setSharedLibsDir);
        setPropertyFromEnv(SHARED_CLASSPATH_DIR, baseConfiguration::setSharedClasspathDir);
        setPropertyFromEnv(TOMCAT_BASE_DIR, baseConfiguration::setTomcatBaseDir);
        setPropertyFromEnv(GENERATED_CLASSPATH_DIR, baseConfiguration::setGeneratedClasspathDir);
        setPropertyFromEnv(TOMCAT_PORT, value -> baseConfiguration.setTomcatPort(Integer.parseInt(value)));
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
