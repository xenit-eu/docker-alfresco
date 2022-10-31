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
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.SHARE_CONFIG_PATH;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.SHARE_CONFIG_TEMPLATE_FILE;

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
        setEnv(ALFRESCO_HOST, alfrescoHost);

    }

    public String getAlfrescoInternalHost() {
        return alfrescoInternalHost;
    }

    public void setAlfrescoInternalHost(String alfrescoInternalHost) {
        this.alfrescoInternalHost = alfrescoInternalHost;
        setEnv(ALFRESCO_INTERNAL_HOST, alfrescoInternalHost);

    }

    public int getAlfrescoPort() {
        return alfrescoPort;
    }

    public void setAlfrescoPort(int alfrescoPort) {
        this.alfrescoPort = alfrescoPort;
        setEnv(ALFRESCO_PORT, String.valueOf(alfrescoPort));

    }

    public int getAlfrescoInternalPort() {
        return alfrescoInternalPort;
    }

    public void setAlfrescoInternalPort(int alfrescoInternalPort) {
        this.alfrescoInternalPort = alfrescoInternalPort;
        setEnv(ALFRESCO_INTERNAL_PORT, String.valueOf(alfrescoInternalPort));

    }

    public String getAlfrescoProtocol() {
        return alfrescoProtocol;
    }

    public void setAlfrescoProtocol(String alfrescoProtocol) {
        this.alfrescoProtocol = alfrescoProtocol;
        setEnv(ALFRESCO_PROTOCOL, alfrescoProtocol);

    }

    public String getAlfrescoInternalProtocol() {
        return alfrescoInternalProtocol;
    }

    public void setAlfrescoInternalProtocol(String alfrescoInternalProtocol) {
        this.alfrescoInternalProtocol = alfrescoInternalProtocol;
        setEnv(ALFRESCO_INTERNAL_PROTOCOL, alfrescoInternalProtocol);

    }

    public String getAlfrescoContext() {
        return alfrescoContext;
    }

    public void setAlfrescoContext(String alfrescoContext) {
        this.alfrescoContext = alfrescoContext;
        setEnv(ALFRESCO_CONTEXT, alfrescoContext);

    }

    public String getAlfrescoInternalContext() {
        return alfrescoInternalContext;
    }

    public void setAlfrescoInternalContext(String alfrescoInternalContext) {
        this.alfrescoInternalContext = alfrescoInternalContext;
        setEnv(ALFRESCO_INTERNAL_CONTEXT, alfrescoInternalContext);

    }

    public String getShareConfigPath() {
        return shareConfigPath;
    }

    public void setShareConfigPath(String shareConfigPath) {
        this.shareConfigPath = shareConfigPath;
        setEnv(SHARE_CONFIG_PATH, shareConfigPath);
    }

    public String getShareConfigTemplateFile() {
        return shareConfigTemplateFile;

    }

    public void setShareConfigTemplateFile(String shareConfigTemplateFile) {
        this.shareConfigTemplateFile = shareConfigTemplateFile;
        setEnv(SHARE_CONFIG_TEMPLATE_FILE, shareConfigTemplateFile);
    }


}
