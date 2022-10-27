package eu.xenit.alfresco.tomcat.embedded.config;

public class Configuration {
    protected boolean exitOnFailure;
    protected String webappsPath;
    protected int port;
    protected int tomcatSslPort;
    protected String logLibraryDir;
    protected boolean jsonLogging;
    protected boolean accessLogging;
    protected String logsLocation;
    protected int tomcatMaxThreads;
    protected int tomcatMaxHttpHeaderSize;
    protected int tomcatServerPort;
    protected String tomcatRelaxedQueryChars;
    protected String tomcatRelaxedPathChars;
    protected boolean alfrescoEnabled ;
    protected boolean shareEnabled ;

    public boolean isAlfrescoEnabled() {
        return alfrescoEnabled;
    }

    public void setAlfrescoEnabled(boolean alfrescoEnabled) {
        this.alfrescoEnabled = alfrescoEnabled;
    }

    public boolean isShareEnabled() {
        return shareEnabled;
    }

    public void setShareEnabled(boolean shareEnabled) {
        this.shareEnabled = shareEnabled;
    }

    public boolean isExitOnFailure() {
        return exitOnFailure;
    }

    public void setExitOnFailure(boolean exitOnFailure) {
        this.exitOnFailure = exitOnFailure;
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

    public int getTomcatSslPort() {
        return tomcatSslPort;
    }

    public void setTomcatSslPort(int tomcatSslPort) {
        this.tomcatSslPort = tomcatSslPort;
    }

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

    public boolean isAccessLogging() {
        return accessLogging;
    }

    public void setAccessLogging(boolean accessLogging) {
        this.accessLogging = accessLogging;
    }

    public String getLogsLocation() {
        return logsLocation;
    }

    public void setLogsLocation(String logsLocation) {
        this.logsLocation = logsLocation;
    }

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

}
