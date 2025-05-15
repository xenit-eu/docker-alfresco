package eu.xenit.docker.alfresco.test;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

public class LoggingTests {


    public static final long WAIT_FOR_LOGS_DUR = Duration.ofSeconds(10).toMillis();
    public static final String ALFRESCO_IMAGE_NAME = "alfresco_image_name";
    public static final String TYPE_FIELD = "type";
    public static final String APPLICATION_TYPE = "application";
    // First 7 lines of the logs are not controlled by docker-alfresco, +3 for some margin
    public static final int SKIP_LINES = 10;
    public static final String KNOWN_NON_JSON_LINE = "ERROR StatusConsoleListener JsonTemplateLayout contains an invalid element or attribute \"pattern\"";
    final Set<String> REQ_COMMON_LOGGING_FIELDS = Set.of("timestamp", "type");
    private static final ObjectMapper objMapper = new ObjectMapper();

    final Set<String> REQ_APPLICATION_LOGGING_FIELDS = union(
            Set.of("loggerName", "severity", "shortMessage", "fullMessage"), REQ_COMMON_LOGGING_FIELDS);

    private static <E> Set<E> union(Set<E> specificFields, Set<E> commonFields) {
        Set<E> result = new HashSet<>(specificFields);
        result.addAll(commonFields);
        return result;
    }

    private Stream<JsonNode> getJsonLogs(String logLines) {
        // TODO: Fix the jsonLayout so the filter is not required
        return logLines.lines()
                .skip(SKIP_LINES)
                .filter(line -> !line.equals(KNOWN_NON_JSON_LINE))
                .map(LoggingTests::parseJsonLine);
    }

    private static JsonNode parseJsonLine(String line) {
        try {
            return objMapper.readTree(line);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse log line to json for line:" + line, e);
        } catch (IOException e) {
            throw new RuntimeException("IO error when parsing line: " + line, e);
        }
    }

    private boolean validateApplicationJsonLog(Stream<JsonNode> jsonNodes) {
        // All json logs of application type, should contain all the fields related to application type logs
        return jsonNodes.filter(logLine -> logLine.has(TYPE_FIELD) && APPLICATION_TYPE.equals(
                        logLine.path(TYPE_FIELD).asText()))
                .allMatch(logLine -> REQ_APPLICATION_LOGGING_FIELDS.stream().allMatch(logLine::has));
    }

    private void setupAlfrescoTestContainer(GenericContainer<?> alfContainer, boolean jsonLogging) {
        alfContainer
                .withExposedPorts(8080)
                .withEnv(Map.of(
                "ACCESS_LOGGING", "false",
                "GLOBAL_legacy.transform.service.enabled", "false",
                "GLOBAL_local.transform.service.enabled", "false",
                "JSON_LOGGING", String.valueOf(jsonLogging)
                ));
    }

    private static DockerImageName getAlfrescoImageName() {
        return DockerImageName.parse(System.getProperty(ALFRESCO_IMAGE_NAME));
    }

    private boolean isJsonLogs(String logs) {
        try {
            List<JsonNode> ignored = getJsonLogs(logs).toList();
        } catch (RuntimeException e) {
            return false;
        }
        return true;


    }

    @Test
    public void testNonJsonLogging() throws InterruptedException {
        try (GenericContainer<?> alfContainer = new GenericContainer<>(getAlfrescoImageName())) {
            setupAlfrescoTestContainer(alfContainer, false);
            alfContainer.start();

            // Sleep to let the logs accumulate
            Thread.sleep(WAIT_FOR_LOGS_DUR);

            String logs = alfContainer.getLogs();

            Assert.assertFalse("Logs should not be json. Logs:\n" + logs, isJsonLogs(logs));
        }
    }

    @Test
    public void testJsonLogging() throws InterruptedException {
        try (GenericContainer<?> alfContainer = new GenericContainer<>(getAlfrescoImageName())) {
            setupAlfrescoTestContainer(alfContainer, true);
            alfContainer.start();

            // Sleep to let the logs accumulate
            Thread.sleep(WAIT_FOR_LOGS_DUR);

            String logs = alfContainer.getLogs();

            // Every line should be json
            Assert.assertTrue("Logs should be json. Logs:\n" + logs, isJsonLogs(logs));
            Stream<JsonNode> jsonNodes = getJsonLogs(logs);
            Assert.assertTrue(validateApplicationJsonLog(jsonNodes));
        }
    }
}
