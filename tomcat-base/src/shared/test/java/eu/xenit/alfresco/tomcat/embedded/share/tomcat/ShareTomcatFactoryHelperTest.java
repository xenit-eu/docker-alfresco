package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.DefaultShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;
import org.junit.jupiter.api.Test;

 class ShareTomcatFactoryHelperTest {
    @Test
    void createShareConfigCustomFileTest() {
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider()
                .getConfiguration(new ShareConfiguration(new DefaultConfigurationProvider()
                        .getConfiguration()));
        shareConfiguration.setShareConfigPath("test");
        shareConfiguration.setClassPathDir("D:/test");
        shareConfiguration.setShareConfigTemplateFile("D:/share-config-custom.xml");
        ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration);
    }
}
