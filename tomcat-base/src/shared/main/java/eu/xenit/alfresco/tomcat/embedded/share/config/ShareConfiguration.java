package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PROTOCOL;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PROTOCOL;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ShareConfiguration {

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

    private TomcatConfiguration tomcatConfiguration;

    public ShareConfiguration(TomcatConfiguration configuration) {
        this.tomcatConfiguration = configuration;
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
                return System.getenv(key);
        }
    }

}
