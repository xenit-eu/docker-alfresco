package eu.xenit.alfresco.tomcat.embedded.config;

public class DefaultConfigurationProvider implements ConfigurationProvider {

    public Configuration getConfiguration(Configuration baseConfiguration) {
        baseConfiguration.setWebappsPath("/usr/local/tomcat/webapps");
        baseConfiguration.setLogsLocation("/usr/local/tomcat/logs");
        baseConfiguration.setPort(8080);
        baseConfiguration.setJsonLogging(false);
        baseConfiguration.setAccessLogging(false);
        baseConfiguration.setSharedClassesPath("/loglibrarydir");
        baseConfiguration.setTomcatMaxThreads(200);
        baseConfiguration.setTomcatMaxHttpHeaderSize(32768);
        baseConfiguration.setTomcatSslPort(8443);
        baseConfiguration.setTomcatServerPort(8005);
        baseConfiguration.setExitOnFailure(true);
        baseConfiguration.setAlfrescoEnabled(false);
        baseConfiguration.setShareEnabled(false);
        return baseConfiguration;
    }
}
