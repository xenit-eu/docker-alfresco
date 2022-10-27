package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

public class ShareConfiguration extends Configuration {

    private String alfrescoHost;
    private String alfrescoInternalHost;
    private int alfrescoPort;
    private int alfrescoInternalPort;
    private String alfrescoProtocol;
    private String alfrescoInternalProtocol;
    private String alfrescoContext;
    private String alfrescoInternalContext;


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


}
