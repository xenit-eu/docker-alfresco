package eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.DefaultAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.ConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;


class AlfrescoTomcatFactoryHelperTest {
    @Test
    void testCreateGlobalPropertiesFile() throws URISyntaxException, IOException {
        TomcatConfiguration tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        AlfrescoConfiguration alfrescoConfiguration = new DefaultAlfrescoConfigurationProvider().getConfiguration(
                new AlfrescoConfiguration(tomcatConfiguration));
        URL expectedResource = getClass().getClassLoader().getResource("alfresco-global-output.properties");
        assert expectedResource != null;
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        tomcatConfiguration.setGeneratedClasspathDir(expectedPath.toAbsolutePath().getParent().toString());
        AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile(alfrescoConfiguration);
        Path tempProps = Paths.get(tomcatConfiguration.getGeneratedClasspathDir(), "alfresco-global.properties");
        String actual = new String(Files.readAllBytes(tempProps), StandardCharsets.UTF_8);
        actual = actual.substring(actual.indexOf("\n") + 1);
        String expected = new String(Files.readAllBytes(expectedPath), StandardCharsets.UTF_8);
        assertEquals(actual, expected);
    }

    @Test
    void testCreateGlobalPropertiesFileWithFileExist() throws URISyntaxException, IOException {
        TomcatConfiguration tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        AlfrescoConfiguration alfrescoConfiguration = new DefaultAlfrescoConfigurationProvider().getConfiguration(
                new AlfrescoConfiguration(tomcatConfiguration));
        URL expectedResource = getClass().getClassLoader().getResource("alfresco-global-output.properties");
        assert expectedResource != null;
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        tomcatConfiguration.setGeneratedClasspathDir(expectedPath.toAbsolutePath().getParent().toString() + "/result-exist");
        AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile(alfrescoConfiguration);
        Path tempProps = Paths.get(tomcatConfiguration.getGeneratedClasspathDir(), "alfresco-global.properties");
        String actual = new String(Files.readAllBytes(tempProps), StandardCharsets.UTF_8);
        actual = actual.substring(actual.indexOf("\n") + 1);
        String expected = new String(Files.readAllBytes(expectedPath), StandardCharsets.UTF_8);
        assertEquals(actual, expected);
    }
}
