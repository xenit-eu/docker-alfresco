package eu.xenit.alfresco.tomcat.embedded.config;

import java.util.HashMap;
import java.util.Map;

import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.ACCESS_LOGGING;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.ALFRESCO_ENABLED;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.CLASSPATH_DIR;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.EXIT_ON_FAILURE;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.JSON_LOGGING;
import static eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariables.LOGLIBRARY_DIR;
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

public class Configuration {
    protected boolean exitOnFailure;
    protected String webappsPath;
    protected String tomcatBaseDir;
    protected int tomcatPort;
    protected int tomcatSslPort;
    protected String logLibraryDir;
    protected boolean jsonLogging;
    protected boolean accessLogging;
    protected int tomcatMaxThreads;
    protected int tomcatMaxHttpHeaderSize;
    protected int tomcatServerPort;
    protected String tomcatRelaxedQueryChars;
    protected String tomcatRelaxedPathChars;
    protected boolean alfrescoEnabled;
    protected boolean shareEnabled;
    protected String classPathDir;
    protected Map<String, String> env = new HashMap<>();
    public Configuration() {
    }
    protected Configuration(Configuration configuration) {
        setExitOnFailure(configuration.isExitOnFailure());
        setWebappsPath(configuration.getWebappsPath());
        setTomcatBaseDir(configuration.getTomcatBaseDir());
        setTomcatPort(configuration.getTomcatPort());
        setTomcatSslPort(configuration.getTomcatSslPort());
        setLogLibraryDir(configuration.getLogLibraryDir());
        setJsonLogging(configuration.isJsonLogging());
        setAccessLogging(configuration.isAccessLogging());
        setTomcatMaxThreads(configuration.getTomcatMaxThreads());
        setTomcatMaxHttpHeaderSize(configuration.getTomcatMaxHttpHeaderSize());
        setTomcatServerPort(configuration.getTomcatServerPort());
        setTomcatRelaxedQueryChars(configuration.getTomcatRelaxedQueryChars());
        setTomcatRelaxedPathChars(configuration.getTomcatRelaxedPathChars());
        setAlfrescoEnabled(configuration.isAlfrescoEnabled());
        setShareEnabled(configuration.isShareEnabled());
        setClassPathDir(configuration.getClassPathDir());
    }

    public boolean isAlfrescoEnabled() {
        return alfrescoEnabled;
    }

    public void setAlfrescoEnabled(boolean alfrescoEnabled) {
        this.alfrescoEnabled = alfrescoEnabled;
        setEnv(ALFRESCO_ENABLED, String.valueOf(alfrescoEnabled));

    }

    public boolean isShareEnabled() {
        return shareEnabled;
    }

    public void setShareEnabled(boolean shareEnabled) {
        this.shareEnabled = shareEnabled;
        setEnv(SHARE_ENABLED, String.valueOf(shareEnabled));

    }

    public boolean isExitOnFailure() {
        return exitOnFailure;
    }

    public void setExitOnFailure(boolean exitOnFailure) {
        this.exitOnFailure = exitOnFailure;
        setEnv(EXIT_ON_FAILURE, String.valueOf(exitOnFailure));

    }

    public String getWebappsPath() {
        return webappsPath;
    }

    public void setWebappsPath(String webappsPath) {
        this.webappsPath = webappsPath;
        setEnv(TOMCAT_WEBAPPS, webappsPath);
    }

    public String getTomcatBaseDir() {
        return tomcatBaseDir;

    }

    public void setTomcatBaseDir(String tomcatBaseDir) {
        this.tomcatBaseDir = tomcatBaseDir;
        setEnv(TOMCAT_BASE_DIR, tomcatBaseDir);

    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public void setTomcatPort(int tomcatPort) {
        this.tomcatPort = tomcatPort;
        setEnv(TOMCAT_PORT, String.valueOf(tomcatPort));

    }

    public int getTomcatSslPort() {
        return tomcatSslPort;
    }

    public void setTomcatSslPort(int tomcatSslPort) {
        this.tomcatSslPort = tomcatSslPort;
        setEnv(TOMCAT_PORT_SSL, String.valueOf(tomcatSslPort));


    }

    public String getLogLibraryDir() {
        return logLibraryDir;
    }

    public void setLogLibraryDir(String logLibraryDir) {
        this.logLibraryDir = logLibraryDir;
        setEnv(LOGLIBRARY_DIR, logLibraryDir);

    }

    public boolean isJsonLogging() {
        return jsonLogging;
    }

    public void setJsonLogging(boolean jsonLogging) {
        this.jsonLogging = jsonLogging;
        setEnv(JSON_LOGGING, String.valueOf(jsonLogging));

    }

    public boolean isAccessLogging() {
        return accessLogging;
    }

    public void setAccessLogging(boolean accessLogging) {
        this.accessLogging = accessLogging;
        setEnv(ACCESS_LOGGING, String.valueOf(accessLogging));
    }


    public int getTomcatMaxThreads() {
        return tomcatMaxThreads;
    }

    public void setTomcatMaxThreads(int tomcatMaxThreads) {
        this.tomcatMaxThreads = tomcatMaxThreads;
        setEnv(TOMCAT_MAX_THREADS, String.valueOf(tomcatMaxThreads));

    }

    public int getTomcatMaxHttpHeaderSize() {
        return tomcatMaxHttpHeaderSize;
    }

    public void setTomcatMaxHttpHeaderSize(int tomcatMaxHttpHeaderSize) {
        this.tomcatMaxHttpHeaderSize = tomcatMaxHttpHeaderSize;
        setEnv(TOMCAT_MAX_HTTP_HEADER_SIZE, String.valueOf(tomcatMaxHttpHeaderSize));

    }

    public int getTomcatServerPort() {
        return tomcatServerPort;
    }

    public void setTomcatServerPort(int tomcatServerPort) {
        this.tomcatServerPort = tomcatServerPort;
        setEnv(TOMCAT_SERVER_PORT, String.valueOf(tomcatServerPort));

    }

    public String getTomcatRelaxedQueryChars() {
        return tomcatRelaxedQueryChars;
    }

    public void setTomcatRelaxedQueryChars(String tomcatRelaxedQueryChars) {
        this.tomcatRelaxedQueryChars = tomcatRelaxedQueryChars;
        setEnv(TOMCAT_RELAXED_QUERY_CHARS, tomcatRelaxedQueryChars);

    }

    public String getTomcatRelaxedPathChars() {
        return tomcatRelaxedPathChars;
    }

    public void setTomcatRelaxedPathChars(String tomcatRelaxedPathChars) {
        this.tomcatRelaxedPathChars = tomcatRelaxedPathChars;
        setEnv(TOMCAT_RELAXED_PATH_CHARS, tomcatRelaxedPathChars);
    }

    public String getClassPathDir() {
        return classPathDir;
    }

    public void setClassPathDir(String classPathDir) {
        this.classPathDir = classPathDir;
        setEnv(CLASSPATH_DIR, classPathDir);
    }

    public void setEnv(String key, String value) {
        env.put(key, value);
    }

    public String getEnv(String key) {
        return env.get(key);
    }

    public Map<String, String> getEnv() {
        return new HashMap<>(env);
    }
}
