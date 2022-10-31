package eu.xenit.alfresco.tomcat.embedded.alfresco.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

import java.util.HashMap;
import java.util.Map;

import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.ALFRESCO_FLAVOUR;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.ALFRESCO_VERSION;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_KEYSTORE;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_KEYSTORE_PASSWORD;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_TRUSTSTORE;
import static eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoEnvironmentVariables.TOMCAT_SSL_TRUSTSTORE_PASSWORD;

public class AlfrescoConfiguration extends Configuration {

    private Map<String, String> globalProperties = new HashMap<>();
    private String alfrescoVersion;
    private String alfrescoFlavour;
    private boolean solrSSLEnabled;
    private String tomcatSSLKeystore;
    private String tomcatSSLKeystorePassword;
    private String tomcatSSLTruststore;
    private String tomcatSSLTruststorePassword;

    public AlfrescoConfiguration() {
    }

    public AlfrescoConfiguration(Configuration configuration) {
        super(configuration);
    }

    public Map<String, String> getGlobalProperties() {
        return globalProperties;
    }

    public void setGlobalProperty(String key, String value) {
        globalProperties.put(key, value);
    }


    public void setSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }


    public String getAlfrescoVersion() {
        return alfrescoVersion;
    }

    public void setAlfrescoVersion(String alfrescoVersion) {
        this.alfrescoVersion = alfrescoVersion;
        setEnv(ALFRESCO_VERSION, alfrescoVersion);
    }

    public String getAlfrescoFlavour() {
        return alfrescoFlavour;
    }

    public void setAlfrescoFlavour(String alfrescoFlavour) {
        this.alfrescoFlavour = alfrescoFlavour;
        setEnv(ALFRESCO_FLAVOUR, alfrescoFlavour);

    }


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
        setEnv(TOMCAT_SSL_KEYSTORE, tomcatSSLKeystore);

    }

    public String getTomcatSSLKeystorePassword() {
        return tomcatSSLKeystorePassword;
    }

    public void setTomcatSSLKeystorePassword(String tomcatSSLKeystorePassword) {
        this.tomcatSSLKeystorePassword = tomcatSSLKeystorePassword;
        setEnv(TOMCAT_SSL_KEYSTORE_PASSWORD, tomcatSSLKeystorePassword);

    }

    public String getTomcatSSLTruststore() {
        return tomcatSSLTruststore;
    }

    public void setTomcatSSLTruststore(String tomcatSSLTruststore) {
        this.tomcatSSLTruststore = tomcatSSLTruststore;
        setEnv(TOMCAT_SSL_TRUSTSTORE, tomcatSSLTruststore);

    }

    public String getTomcatSSLTruststorePassword() {
        return tomcatSSLTruststorePassword;
    }

    public void setTomcatSSLTruststorePassword(String tomcatSSLTruststorePassword) {
        this.tomcatSSLTruststorePassword = tomcatSSLTruststorePassword;
        setEnv(TOMCAT_SSL_TRUSTSTORE_PASSWORD, tomcatSSLTruststorePassword);

    }
}
