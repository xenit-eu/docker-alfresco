package eu.xenit.alfresco.tomcat.embedded.share.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.DefaultShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ShareTomcatFactoryHelperTest {
    @Test
    void testCreateShareConfigCustomFile() throws URISyntaxException, IOException {
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider().getConfiguration(
                new ShareConfiguration(
                        new DefaultConfigurationProvider().getConfiguration()));
        URL inputResource = getClass().getClassLoader().getResource("share-config-custom-input.xml");
        URL expectedResource = getClass().getClassLoader().getResource("share-config-custom-output.xml");
        assert inputResource != null;
        assert expectedResource != null;
        Path inputPath = Paths.get(new File(inputResource.toURI()).getPath());
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        shareConfiguration.setShareConfigTemplateFile(inputPath.toString());
        shareConfiguration.setGeneratedClasspathDir(inputPath.toAbsolutePath().getParent().toString());
        shareConfiguration.setShareConfigPath("result");
        assertTrue(ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration));
        Path tempProps = Paths.get(shareConfiguration.getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath(), "share-config-custom.xml");
        String actual = Files.readString(tempProps);
        String expected = Files.readString(expectedPath);
        assertEquals(actual, expected);
    }

    @Test
    void testCreateShareConfigCustomFileWithFileExist() throws URISyntaxException, IOException {
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider().getConfiguration(
                new ShareConfiguration(
                        new DefaultConfigurationProvider().getConfiguration()));
        URL inputResource = getClass().getClassLoader().getResource("share-config-custom-input.xml");
        URL expectedResource = getClass().getClassLoader().getResource("share-config-custom-output.xml");
        assert inputResource != null;
        assert expectedResource != null;
        Path inputPath = Paths.get(new File(inputResource.toURI()).getPath());
        Path expectedPath = Paths.get(new File(expectedResource.toURI()).getPath());
        shareConfiguration.setShareConfigTemplateFile(inputPath.toString());
        shareConfiguration.setGeneratedClasspathDir(inputPath.toAbsolutePath().getParent().toString());
        shareConfiguration.setShareConfigPath("result-exist");
        assertTrue(ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration));
        Path tempProps = Paths.get(shareConfiguration.getGeneratedClasspathDir(), shareConfiguration.getShareConfigPath(), "share-config-custom.xml");
        String actual = Files.readString(tempProps);
        String expected = Files.readString(expectedPath);
        assertEquals(actual, expected);
    }

    @Test
    void testCreateShareConfigCustomFileNoInputFile() {
        ShareConfiguration shareConfiguration = new DefaultShareConfigurationProvider().getConfiguration(
                new ShareConfiguration(
                        new DefaultConfigurationProvider().getConfiguration()));
        shareConfiguration.setShareConfigTemplateFile("/do/nothing/test.xml");
        shareConfiguration.setGeneratedClasspathDir("/not_gonna_be_called");
        shareConfiguration.setShareConfigPath("result");
        assertFalse(ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration));
    }
}
