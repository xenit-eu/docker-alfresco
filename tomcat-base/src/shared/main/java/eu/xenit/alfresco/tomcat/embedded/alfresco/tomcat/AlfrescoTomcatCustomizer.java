package eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.DefaultAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.EnvironmentVariableAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat.AlfrescoTomcatFactoryHelper;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatCustomizer;
import org.apache.catalina.startup.Tomcat;

public class AlfrescoTomcatCustomizer implements TomcatCustomizer {

    @Override
    public void customize(Tomcat tomcat, TomcatConfiguration configuration) {
        AlfrescoConfiguration
                alfrescoConfiguration = new EnvironmentVariableAlfrescoConfigurationProvider()
                .getConfiguration(new DefaultAlfrescoConfigurationProvider()
                        .getConfiguration(new AlfrescoConfiguration(configuration)));
        AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile(alfrescoConfiguration);
        if (alfrescoConfiguration.isSolrSSLEnabled()) {
            AlfrescoTomcatFactoryHelper.createSSLConnector(tomcat, alfrescoConfiguration);
        }
    }
}
