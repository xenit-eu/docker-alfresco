package eu.xenit.alfresco.tomcat.embedded.alfresco.config;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlfrescoConfiguration extends Configuration {

    private Map<String, String> globalProperties = new HashMap<>();
    private String alfrescoVersion;
    private String alfrescoFlavour;
    private boolean solrSSLEnabled;
    private String tomcatSSLKeystore;
    private String tomcatSSLKeystorePassword;
    private String tomcatSSLTruststore;
    private String tomcatSSLTruststorePassword;

    public AlfrescoConfiguration(Configuration configuration) {
        super(configuration);
    }

    public void setGlobalProperty(String key, String value) {
        globalProperties.put(key, value);
    }

    public void setSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }
}
