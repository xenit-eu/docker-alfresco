package eu.xenit.alfresco.tomcat.embedded.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

class UtilsTest {

    @Test
    void redirectLog4jTest() throws Exception {

        URL inputFolderFailing = getClass().getClassLoader().getResource("result-exist");
        assert inputFolderFailing != null;
        Path inputPathFailing = Paths.get(new File(inputFolderFailing.toURI()).getPath());
        Assertions.assertNull(System.getProperty("log4j.configuration"));
        Utils.redirectLog4j(inputPathFailing);
        Assertions.assertNull(System.getProperty("log4j.configuration"));
        URL inputFolder = getClass().getClassLoader().getResource("utils");
        assert inputFolder != null;
        Path inputPath = Paths.get(new File(inputFolder.toURI()).getPath());
        Utils.redirectLog4j(inputPath);
        Assertions.assertNotNull(System.getProperty("log4j.configuration"));

    }
}
