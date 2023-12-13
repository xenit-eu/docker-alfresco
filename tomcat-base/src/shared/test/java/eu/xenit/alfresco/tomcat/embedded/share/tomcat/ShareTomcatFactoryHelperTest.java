package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.share.config.DefaultShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class ShareTomcatFactoryHelperTest {
    @Test
    void testCreateShareConfigCustomFile() throws URISyntaxException, IOException {
        checkShareConfigCustom("result", "share-config-custom-input.xml", "share-config-custom-output.xml");
    }

    private void checkShareConfigCustom(String shareConfigPath, String shareConfigCustomTemplate, String shareConfigCustomRendered) throws URISyntaxException, IOException {
        TomcatConfiguration tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider().getConfiguration(
                new ShareConfiguration(tomcatConfiguration));
        URL inputResource = getClass().getClassLoader().getResource(shareConfigCustomTemplate);
        URL expectedResource = getClass().getClassLoader().getResource(shareConfigCustomRendered);
        assert inputResource != null;
        assert expectedResource != null;
        Path inputPath = Paths.get(new File(inputResource.toURI()).getPath());
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        shareConfiguration.setShareConfigTemplateFile(inputPath.toString());
        tomcatConfiguration.setGeneratedClasspathDir(inputPath.toAbsolutePath().getParent().toString());
        shareConfiguration.setShareConfigPath(shareConfigPath);
        ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration);
        Path tempProps = Paths.get(tomcatConfiguration.getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath(),
                "share-config-custom.xml");
        String actual = new String(Files.readAllBytes(tempProps), StandardCharsets.UTF_8);
        String expected = new String(Files.readAllBytes(expectedPath), StandardCharsets.UTF_8);
        assertEquals(actual, expected);
    }

    @Test
    void testCreateShareConfigCustomFileWithFileExist() throws URISyntaxException, IOException {
        checkShareConfigCustom("result-exist", "share-config-custom-input.xml", "share-config-custom-output.xml");
    }

    @Test
    void testCreateShareConfigCustomFileNoInputFile() {
        TomcatConfiguration tomcatConfiguration = new DefaultConfigurationProvider().getConfiguration();
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider().getConfiguration(
                new ShareConfiguration(tomcatConfiguration));
        shareConfiguration.setShareConfigTemplateFile("/do/nothing/test.xml");
        tomcatConfiguration.setGeneratedClasspathDir("/not_gonna_be_called");
        shareConfiguration.setShareConfigPath("result");
        ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration);
        Path fileNotExisiting = Paths.get(shareConfiguration.getTomcatConfiguration().getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath(), "share-config-custom.xml");
        assertFalse(Files.exists(fileNotExisiting));
    }

    @Test
    @SetEnvironmentVariable(key = "MY_CUSTOM_env", value = "http://localhost:8083/activiti-app/api/enterprise")
    void testShareConfigCustomWithCustomEnvVar() throws URISyntaxException, IOException {
        checkShareConfigCustom("testcustomenv", "share-config-custom-custom-env/share-config-custom-template.xml", "share-config-custom-custom-env/share-config-custom.xml");
    }


}
