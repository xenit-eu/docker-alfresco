package eu.xenit.alfresco.tomcat.embedded.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private String webappsPath;
    private int port;
    private String logLibraryDir;
    private boolean enableJsonLogging;
    private Map<String, String> globalProperties = new HashMap<>();
    private String alfrescoVersion;
    private String alfrescoFlavour;
    private String logsLocation;
    private String alfrescoLocation;

    public String getLogLibraryDir() {
        return logLibraryDir;
    }

    public void setLogLibraryDir(String logLibraryDir) {
        this.logLibraryDir = logLibraryDir;
    }

    public boolean isEnableJsonLogging() {
        return enableJsonLogging;
    }

    public void setEnableJsonLogging(boolean enableJsonLogging) {
        this.enableJsonLogging = enableJsonLogging;
    }

    public Map<String, String> getGlobalProperties() {
        return globalProperties;
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
}
