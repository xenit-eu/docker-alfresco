package eu.xenit.alfresco.tomcat.embedded.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private String webappsPath;
    private int port;

    public String getLogLibraryDir() {
        return logLibraryDir;
    }

    public void setLogLibraryDir(String logLibraryDir) {
        this.logLibraryDir = logLibraryDir;
    }

    private String logLibraryDir;

    public boolean isEnableJsonLogging() {
        return enableJsonLogging;
    }

    public void setEnableJsonLogging(boolean enableJsonLogging) {
        this.enableJsonLogging = enableJsonLogging;
    }

    private boolean enableJsonLogging;

    public Map<String, String> getGlobalProperties() {
        return globalProperties;
    }

    private Map<String, String> globalProperties = new HashMap<>();

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

    private String alfrescoVersion;

    public String getAlfrescoVersion() {
        return alfrescoVersion;
    }

    public void setAlfrescoVersion(String alfrescoVersion) {
        this.alfrescoVersion = alfrescoVersion;
    }

    private String alfrescoFlavour;

    public String getAlfrescoFlavour() {
        return alfrescoFlavour;
    }

    public void setAlfrescoFlavour(String alfrescoFlavour) {
        this.alfrescoFlavour = alfrescoFlavour;
    }

    private String logsLocation;

    public String getLogsLocation() {
        return logsLocation;
    }

    public void setLogsLocation(String logsLocation) {
        this.logsLocation = logsLocation;
    }
}
