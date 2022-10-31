package eu.xenit.alfresco.tomcat.embedded.config;

public class Configuration {
    protected boolean exitOnFailure;
    protected String webappsPath;
    protected String tomcatBaseDir;
    protected int tomcatPort;
    protected int tomcatSslPort;
    protected String sharedClasspathDir;
    protected String sharedLibsDir;
    protected boolean jsonLogging;
    protected boolean accessLogging;
    protected int tomcatMaxThreads;
    protected int tomcatMaxHttpHeaderSize;
    protected int tomcatServerPort;
    protected String tomcatRelaxedQueryChars;
    protected String tomcatRelaxedPathChars;
    protected boolean alfrescoEnabled;
    protected boolean shareEnabled;
    protected String generatedClasspathDir;

    public Configuration() {
    }

    protected Configuration(Configuration configuration) {
        setExitOnFailure(configuration.isExitOnFailure());
        setWebappsPath(configuration.getWebappsPath());
        setTomcatBaseDir(configuration.getTomcatBaseDir());
        setTomcatPort(configuration.getTomcatPort());
        setTomcatSslPort(configuration.getTomcatSslPort());
        setSharedClasspathDir(configuration.getSharedClasspathDir());
        setJsonLogging(configuration.isJsonLogging());
        setAccessLogging(configuration.isAccessLogging());
        setTomcatMaxThreads(configuration.getTomcatMaxThreads());
        setTomcatMaxHttpHeaderSize(configuration.getTomcatMaxHttpHeaderSize());
        setTomcatServerPort(configuration.getTomcatServerPort());
        setTomcatRelaxedQueryChars(configuration.getTomcatRelaxedQueryChars());
        setTomcatRelaxedPathChars(configuration.getTomcatRelaxedPathChars());
        setAlfrescoEnabled(configuration.isAlfrescoEnabled());
        setShareEnabled(configuration.isShareEnabled());
        setGeneratedClasspathDir(configuration.getGeneratedClasspathDir());
    }

    public String getSharedLibsDir() {
        return sharedLibsDir;
    }

    public void setSharedLibsDir(String sharedLibsDir) {
        this.sharedLibsDir = sharedLibsDir;
    }

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

    public String getTomcatBaseDir() {
        return tomcatBaseDir;

    }

    public void setTomcatBaseDir(String tomcatBaseDir) {
        this.tomcatBaseDir = tomcatBaseDir;
    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public void setTomcatPort(int tomcatPort) {
        this.tomcatPort = tomcatPort;
    }

    public int getTomcatSslPort() {
        return tomcatSslPort;
    }

    public void setTomcatSslPort(int tomcatSslPort) {
        this.tomcatSslPort = tomcatSslPort;

    }

    public String getSharedClasspathDir() {
        return sharedClasspathDir;
    }

    public void setSharedClasspathDir(String sharedClasspathDir) {
        this.sharedClasspathDir = sharedClasspathDir;
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

    public String getGeneratedClasspathDir() {
        return generatedClasspathDir;
    }

    public void setGeneratedClasspathDir(String generatedClasspathDir) {
        this.generatedClasspathDir = generatedClasspathDir;
    }
}
