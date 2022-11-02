package eu.xenit.alfresco.tomcat.embedded.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
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
        setSharedLibsDir(configuration.getSharedLibsDir());
    }
}
