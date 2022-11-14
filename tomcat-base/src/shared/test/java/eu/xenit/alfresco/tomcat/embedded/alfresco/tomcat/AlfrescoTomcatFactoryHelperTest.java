package eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.DefaultAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;


class AlfrescoTomcatFactoryHelperTest {
    @Test
    void testCreateGlobalPropertiesFile() throws URISyntaxException, IOException {
        var tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        AlfrescoConfiguration alfrescoConfiguration = new DefaultAlfrescoConfigurationProvider().getConfiguration(
                new AlfrescoConfiguration(tomcatConfiguration));
        URL expectedResource = getClass().getClassLoader().getResource("alfresco-global-output.properties");
        assert expectedResource != null;
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        tomcatConfiguration.setGeneratedClasspathDir(expectedPath.toAbsolutePath().getParent().toString());
        AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile(alfrescoConfiguration);
        Path tempProps = Paths.get(tomcatConfiguration.getGeneratedClasspathDir(), "alfresco-global.properties");
        String actual = Files.readString(tempProps);
        actual = actual.substring(actual.indexOf("\n") + 1);
        String expected = Files.readString(expectedPath);
        assertEquals(actual, expected);
    }

    @Test
    void testCreateGlobalPropertiesFileWithFileExist() throws URISyntaxException, IOException {
        var tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        AlfrescoConfiguration alfrescoConfiguration = new DefaultAlfrescoConfigurationProvider().getConfiguration(
                new AlfrescoConfiguration(tomcatConfiguration));
        URL expectedResource = getClass().getClassLoader().getResource("alfresco-global-output.properties");
        assert expectedResource != null;
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        tomcatConfiguration.setGeneratedClasspathDir(expectedPath.toAbsolutePath().getParent().toString() + "/result-exist");
        AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile(alfrescoConfiguration);
        Path tempProps = Paths.get(tomcatConfiguration.getGeneratedClasspathDir(), "alfresco-global.properties");
        String actual = Files.readString(tempProps);
        actual = actual.substring(actual.indexOf("\n") + 1);
        String expected = Files.readString(expectedPath);
        assertEquals(actual, expected);
    }
}
