package eu.xenit.alfresco.tomcat.embedded.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private Utils() {
    }

    public static void setPropertyFromEnv(String env, Consumer<String> stringConsumer) {
        if (System.getenv(env) != null) {
            stringConsumer.accept(System.getenv(env));
        }
    }

    public static void redirectLog4j(Path webappPath, Path destinationPath) {
        Path log4JPropertiesPath = getLog4JPropertiesPath(webappPath);
        if (log4JPropertiesPath == null) return;

        boolean isLog4j2 = log4JPropertiesPath.endsWith("log4j2.properties");
        Properties properties = new Properties();
        try {
            try (var reader = Files.newBufferedReader(log4JPropertiesPath)) {
                properties.load(reader);
                if (isLog4j2) {
                    // log4j 2.x properties
                    setLog4j2Properties(properties, webappPath);
                }
                else {
                    // log4j 1.x properties
                    setLog4j1Properties(properties, webappPath);
                }
                Path tempProps = Files.createTempFile(destinationPath, "log4j-", ".properties");
                try (OutputStream os = Files.newOutputStream(tempProps)) {
                    properties.store(os, null);
                }

                System.setProperty("log4j.configuration", "file:" + tempProps.toAbsolutePath());
                System.setProperty("log4j2.configurationFile", "file:" + tempProps.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setLog4j2Properties(Properties properties, Path webappPath) {
        properties.setProperty("rootLogger.level", "error");
        properties.setProperty("rootLogger.appenderRefs", "stdout");
        properties.setProperty("rootLogger.appenderRef.stdout.ref", "ConsoleAppender");
        properties.setProperty("appender.console.type", "Console");
        properties.setProperty("appender.console.name", "ConsoleAppender");
        properties.setProperty("appender.console.layout.type", "JsonTemplateLayout");
        properties.setProperty("appender.console.layout.eventTemplateUri"
                , "file:///usr/local/tomcat/log4j2/jsonLayout/jsonLayout.json");
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[0].type", "EventTemplateAdditionalField");
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[0].key", "component");
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[0].value", webappPath.getFileName().toString());
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[1].type", "EventTemplateAdditionalField");
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[1].key", "type");
        properties.setProperty("appender.console.layout.eventTemplateAdditionalField[1].value", "application");
    }

    private static void setLog4j1Properties(Properties properties, Path webappPath) {
        properties.setProperty("log4j.rootLogger", "error, Console, jmxlogger1");
        properties.setProperty("log4j.appender.Console.layout", "eu.xenit.logging.json.log4j.JsonLayout");
        properties.setProperty("log4j.appender.Console.layout.Type", "application");
        properties.setProperty("log4j.appender.Console.layout.Component", webappPath.getFileName().toString());
        properties.setProperty("log4j.appender.Console.layout.ExtractStackTrace", "true");
        properties.setProperty("log4j.appender.Console.layout.FilterStackTrace", "true");
    }

    private static Path getLog4JPropertiesPath(Path webappPath) {
        List<String> log4jPathToTry = List.of("WEB-INF/classes/log4j.properties",
                "WEB-INF/classes/log4j2.properties");
        List<Path> log4jPaths = new ArrayList<>();
        for (String path : log4jPathToTry) {
            Path log4JPropertiesPath = webappPath.resolve(path);
            log4jPaths.add(log4JPropertiesPath);
            if (Files.exists(log4JPropertiesPath)) {
                return log4JPropertiesPath;
            }
        }
        LOG.warning("Log4j file doesn't exist under paths : " +
                log4jPaths.stream().map(Path::toString).collect(Collectors.joining(" ,")));
        return null;
    }
}
