package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.share.config.DefaultShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.EnvironmentVariableShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;
import eu.xenit.alfresco.tomcat.embedded.share.tomcat.ShareTomcatFactoryHelper;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatCustomizer;
import org.apache.catalina.startup.Tomcat;

public class ShareTomcatCustomizer implements TomcatCustomizer {

    @Override
    public void customize(Tomcat tomcat, TomcatConfiguration configuration) {
        ShareConfiguration shareConfiguration = new EnvironmentVariableShareConfigurationProvider()
                .getConfiguration(new DefaultShareConfigurationProvider()
                        .getConfiguration(new ShareConfiguration(configuration)));
        ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration);
    }
}
