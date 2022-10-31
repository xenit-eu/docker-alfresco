package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PROTOCOL;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PROTOCOL;

public class ShareConfiguration extends Configuration {

    private String alfrescoHost;
    private String alfrescoInternalHost;
    private int alfrescoPort;
    private int alfrescoInternalPort;
    private String alfrescoProtocol;
    private String alfrescoInternalProtocol;
    private String alfrescoContext;
    private String alfrescoInternalContext;
    private String shareConfigPath;
    private String shareConfigTemplateFile;

    public ShareConfiguration() {

    }

    public ShareConfiguration(Configuration configuration) {
        super(configuration);
    }

    public String getAlfrescoHost() {
        return alfrescoHost;
    }

    public void setAlfrescoHost(String alfrescoHost) {
        this.alfrescoHost = alfrescoHost;

    }

    public String getAlfrescoInternalHost() {
        return alfrescoInternalHost;
    }

    public void setAlfrescoInternalHost(String alfrescoInternalHost) {
        this.alfrescoInternalHost = alfrescoInternalHost;
    }

    public int getAlfrescoPort() {
        return alfrescoPort;
    }

    public void setAlfrescoPort(int alfrescoPort) {
        this.alfrescoPort = alfrescoPort;
    }

    public int getAlfrescoInternalPort() {
        return alfrescoInternalPort;
    }

    public void setAlfrescoInternalPort(int alfrescoInternalPort) {
        this.alfrescoInternalPort = alfrescoInternalPort;
    }

    public String getAlfrescoProtocol() {
        return alfrescoProtocol;
    }

    public void setAlfrescoProtocol(String alfrescoProtocol) {
        this.alfrescoProtocol = alfrescoProtocol;
    }

    public String getAlfrescoInternalProtocol() {
        return alfrescoInternalProtocol;
    }

    public void setAlfrescoInternalProtocol(String alfrescoInternalProtocol) {
        this.alfrescoInternalProtocol = alfrescoInternalProtocol;
    }

    public String getAlfrescoContext() {
        return alfrescoContext;
    }

    public void setAlfrescoContext(String alfrescoContext) {
        this.alfrescoContext = alfrescoContext;
    }

    public String getAlfrescoInternalContext() {
        return alfrescoInternalContext;
    }

    public void setAlfrescoInternalContext(String alfrescoInternalContext) {
        this.alfrescoInternalContext = alfrescoInternalContext;
    }

    public String getShareConfigPath() {
        return shareConfigPath;
    }

    public void setShareConfigPath(String shareConfigPath) {
        this.shareConfigPath = shareConfigPath;
    }

    public String getShareConfigTemplateFile() {
        return shareConfigTemplateFile;
    }

    public void setShareConfigTemplateFile(String shareConfigTemplateFile) {
        this.shareConfigTemplateFile = shareConfigTemplateFile;
    }

    public String getValueOf(String key) {
        switch (key) {
            case ALFRESCO_HOST:
                return getAlfrescoHost();
            case ALFRESCO_PORT:
                return String.valueOf(getAlfrescoPort());
            case ALFRESCO_PROTOCOL:
                return getAlfrescoProtocol();
            case ALFRESCO_CONTEXT:
                return getAlfrescoContext();
            case ALFRESCO_INTERNAL_HOST:
                return getAlfrescoInternalHost();
            case ALFRESCO_INTERNAL_PORT:
                return String.valueOf(getAlfrescoInternalPort());
            case ALFRESCO_INTERNAL_PROTOCOL:
                return getAlfrescoInternalProtocol();
            case ALFRESCO_INTERNAL_CONTEXT:
                return getAlfrescoInternalContext();
            default:
                return null;
        }


    }

}
