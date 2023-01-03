package eu.xenit.alfresco.tomcat.embedded.alfresco.config;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AlfrescoConfiguration {

    private Map<String, String> globalProperties = new HashMap<>();
    private String alfrescoVersion;
    private String alfrescoFlavour;
    private boolean solrSSLEnabled;
    private String tomcatSSLKeystore;
    private String tomcatSSLKeystorePassword;
    private String tomcatSSLTruststore;
    private String tomcatSSLTruststorePassword;

    private TomcatConfiguration tomcatConfiguration;

    public AlfrescoConfiguration(TomcatConfiguration configuration) {
        this.tomcatConfiguration = configuration;
    }

    public void setGlobalProperty(String key, String value) {
        globalProperties.put(key, value);
    }

    public void setSystemProperty(String key, String value) {
        if (System.getProperty(key) == null) {
            System.setProperty(key, value);
        }
    }
}
