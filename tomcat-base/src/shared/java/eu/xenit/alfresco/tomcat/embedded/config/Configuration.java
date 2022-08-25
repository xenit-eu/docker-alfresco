package eu.xenit.alfresco.tomcat.embedded.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private boolean exitOnFailure;

    private String webappsPath;
    private int port;

    public int getTomcatSslPort() {
        return tomcatSslPort;
    }

    public void setTomcatSslPort(int tomcatSslPort) {
        this.tomcatSslPort = tomcatSslPort;
    }

    private int tomcatSslPort;
    private String logLibraryDir;
    private boolean jsonLogging;

    public boolean isAccessLogging() {
        return accessLogging;
    }

    public void setAccessLogging(boolean accessLogging) {
        this.accessLogging = accessLogging;
    }

    private boolean accessLogging;
    private Map<String, String> globalProperties = new HashMap<>();
    private Map<String, String> systemProperties = new HashMap<>();
    private String alfrescoVersion;
    private String alfrescoFlavour;
    private String logsLocation;
    private String alfrescoLocation;
    private boolean solrSSLEnabled;

    public boolean isSolrSSLEnabled() {
        return solrSSLEnabled;
    }

    public void setSolrSSLEnabled(boolean solrSSLEnabled) {
        this.solrSSLEnabled = solrSSLEnabled;
    }

    public String getTomcatSSLKeystore() {
        return tomcatSSLKeystore;
    }

    public void setTomcatSSLKeystore(String tomcatSSLKeystore) {
        this.tomcatSSLKeystore = tomcatSSLKeystore;
    }

    public String getTomcatSSLKeystorePassword() {
        return tomcatSSLKeystorePassword;
    }

    public void setTomcatSSLKeystorePassword(String tomcatSSLKeystorePassword) {
        this.tomcatSSLKeystorePassword = tomcatSSLKeystorePassword;
    }

    public String getTomcatSSLTruststore() {
        return tomcatSSLTruststore;
    }

    public void setTomcatSSLTruststore(String tomcatSSLTruststore) {
        this.tomcatSSLTruststore = tomcatSSLTruststore;
    }

    public String getTomcatSSLTruststorePassword() {
        return tomcatSSLTruststorePassword;
    }

    public void setTomcatSSLTruststorePassword(String tomcatSSLTruststorePassword) {
        this.tomcatSSLTruststorePassword = tomcatSSLTruststorePassword;
    }

    private String tomcatSSLKeystore;
    private String tomcatSSLKeystorePassword;
    private String tomcatSSLTruststore;
    private String tomcatSSLTruststorePassword;

    public int getTomcatMaxThreads() {
        return tomcatMaxThreads;
    }

    public void setTomcatMaxThreads(int tomcatMaxThreads) {
        this.tomcatMaxThreads = tomcatMaxThreads;
    }

    public int getTomcatMaxHttpHeaderSize() {
        return tomcatMaxHttpHeaderSize;
    }

    public void setTomcatMaxHttpHeaderSize(int tomcatMaxHttpHeaderSize) {
        this.tomcatMaxHttpHeaderSize = tomcatMaxHttpHeaderSize;
    }

    public int getTomcatServerPort() {
        return tomcatServerPort;
    }

    public void setTomcatServerPort(int tomcatServerPort) {
        this.tomcatServerPort = tomcatServerPort;
    }

    public String getTomcatRelaxedQueryChars() {
        return tomcatRelaxedQueryChars;
    }

    public void setTomcatRelaxedQueryChars(String tomcatRelaxedQueryChars) {
        this.tomcatRelaxedQueryChars = tomcatRelaxedQueryChars;
    }

    public String getTomcatRelaxedPathChars() {
        return tomcatRelaxedPathChars;
    }

    public void setTomcatRelaxedPathChars(String tomcatRelaxedPathChars) {
        this.tomcatRelaxedPathChars = tomcatRelaxedPathChars;
    }

    private int tomcatMaxThreads;
    private int tomcatMaxHttpHeaderSize;
    private int tomcatServerPort;
    private String tomcatRelaxedQueryChars;
    private String tomcatRelaxedPathChars;

    public String getLogLibraryDir() {
        return logLibraryDir;
    }

    public void setLogLibraryDir(String logLibraryDir) {
        this.logLibraryDir = logLibraryDir;
    }

    public boolean isJsonLogging() {
        return jsonLogging;
    }

    public void setJsonLogging(boolean jsonLogging) {
        this.jsonLogging = jsonLogging;
    }

    public Map<String, String> getGlobalProperties() {
        return globalProperties;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public String getWebappsPath() {
        return webappsPath;
    }

    public void setWebappsPath(String webappsPath) {
        this.webappsPath = webappsPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setGlobalProperty(String key, String value) {
        globalProperties.put(key, value);
    }

    public void setSystemProperty(String key, String value) {
        systemProperties.put(key, value);
    }

    public String getAlfrescoVersion() {
        return alfrescoVersion;
    }

    public void setAlfrescoVersion(String alfrescoVersion) {
        this.alfrescoVersion = alfrescoVersion;
    }

    public String getAlfrescoFlavour() {
        return alfrescoFlavour;
    }

    public void setAlfrescoFlavour(String alfrescoFlavour) {
        this.alfrescoFlavour = alfrescoFlavour;
    }

    public String getLogsLocation() {
        return logsLocation;
    }

    public void setLogsLocation(String logsLocation) {
        this.logsLocation = logsLocation;
    }

    public String getAlfrescoLocation() {
        return alfrescoLocation;
    }

    public void setAlfrescoLocation(String alfrescoLocation) {
        this.alfrescoLocation = alfrescoLocation;
    }

    public boolean isExitOnFailure() {
        return exitOnFailure;
    }

    public void setExitOnFailure(boolean exitOnFailure) {
        this.exitOnFailure = exitOnFailure;
    }
}
