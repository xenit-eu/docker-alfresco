package eu.xenit.alfresco.tomcat.embedded.utils;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Logger;


public class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private Utils() {
    }

    public static void setPropertyFromEnv(String env, Consumer<String> stringConsumer) {
        if (System.getenv(env) != null) {
            stringConsumer.accept(System.getenv(env));
        }
    }

    public static void redirectLog4j(Path path, TomcatConfiguration configuration) {
        Path log4JPropertiesPath = path.resolve("WEB-INF/classes/log4j.properties");
        if (!Files.exists(log4JPropertiesPath)) {
            LOG.warning("Log4j file doesn't exist under path " + log4JPropertiesPath);
            return;
        }
        Properties properties = new Properties();
        try {
            try (var reader = Files.newBufferedReader(log4JPropertiesPath)) {
                properties.load(reader);
                properties.setProperty("log4j.rootLogger", "error, Console, jmxlogger1");
                properties.setProperty("log4j.appender.Console.layout", "eu.xenit.json.log4j.JsonLayout");
                properties.setProperty("log4j.appender.Console.layout.Type", "application");
                properties.setProperty("log4j.appender.Console.layout.Component", path.getFileName().toString());
                properties.setProperty("log4j.appender.Console.layout.ExtractStackTrace", "true");
                properties.setProperty("log4j.appender.Console.layout.FilterStackTrace", "true");
                Path tempProps = Files.createTempFile(Paths.get(configuration.getGeneratedClasspathDir()), "log4j-", ".properties");
                try (OutputStream os = Files.newOutputStream(tempProps)) {
                    properties.store(os, null);
                }
                System.setProperty("log4j.configuration", "file:" + tempProps.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
