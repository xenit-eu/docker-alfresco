package eu.xenit.alfresco.tomcat.embedded.share.config;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
