package eu.xenit.alfresco.tomcat.embedded.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void redirectLog4jTest() throws Exception {
        Path parent = Files.createTempDirectory("SuccessDir");
        Files.createFile(Paths.get(
                Files.createDirectories(Paths.get(
                                parent.toAbsolutePath()
                                        .toString()
                                , "WEB-INF/classes"))
                        .toAbsolutePath()
                        .toString(),
                "log4j.properties"));
        Assertions.assertNull(System.getProperty("log4j.configuration"));
        Utils.redirectLog4j(parent, Files.createTempDirectory("redirectLog4jTest"));
        String log4JFilePath = System.getProperty("log4j.configuration");
        Assertions.assertNotNull(log4JFilePath);
        Assertions.assertTrue(log4JFilePath.split("file:").length > 1);
        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(Paths.get(log4JFilePath.split("file:")[1]))) {
            properties.load(reader);
            Assertions.assertEquals("error, Console, jmxlogger1", properties.getProperty("log4j.rootLogger"));
            Assertions.assertEquals("eu.xenit.json.log4j.JsonLayout", properties.getProperty("log4j.appender.Console.layout"));
            Assertions.assertEquals("application", properties.getProperty("log4j.appender.Console.layout.Type"));
            Assertions.assertEquals(parent.getFileName().toString(), properties.getProperty("log4j.appender.Console.layout.Component"));
            Assertions.assertEquals("true", properties.getProperty("log4j.appender.Console.layout.ExtractStackTrace"));
            Assertions.assertEquals("true", properties.getProperty("log4j.appender.Console.layout.FilterStackTrace"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.clearProperty("log4j.configuration");

    }

    @Test
    void redirectLog4jTestNoPath() throws Exception {
        Path parent = Files.createTempDirectory("FailingDir");
        Assertions.assertNull(System.getProperty("log4j.configuration"));
        Utils.redirectLog4j(parent, Files.createTempDirectory("redirectLog4jTestNoPath"));
        Assertions.assertNull(System.getProperty("log4j.configuration"));
    }
}
