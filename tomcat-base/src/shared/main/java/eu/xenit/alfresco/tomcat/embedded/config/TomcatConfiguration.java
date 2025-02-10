package eu.xenit.alfresco.tomcat.embedded.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TomcatConfiguration {
    protected boolean exitOnFailure;
    protected String webappsPath;
    protected String tomcatBaseDir;
    protected int tomcatPort;
    protected int tomcatSslPort;
    protected String sharedClasspathDir;
    protected String sharedLibDir;
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
    protected long tomcatCacheMaxSize;
    protected boolean allowCasualMultipartParsing;
    protected boolean allowMultipleLeadingForwardSlashInPath;
    protected boolean crossContext;
}
