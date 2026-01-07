package eu.xenit.docker.alfresco.test;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
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
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

public class LoggingTests {

    public static final String ALFRESCO_IMAGE_NAME = "alfresco_image_name";
    public static final String TYPE_FIELD = "type";
    public static final String APPLICATION_TYPE = "application";
    public static final int MIN_LOG_LINES = 30;
    // First 7 lines of the logs are not controlled by docker-alfresco, +3 for some margin
    public static final int SKIP_LINES = 10;
    // DOCKER-467 Error in the jsonLayout always shows this error log
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
                .filter(line -> !line.endsWith(KNOWN_NON_JSON_LINE))
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

    private void setupAlfrescoTestContainer(GenericContainer<?> alfContainer, boolean jsonLogging, Map<String, String> env) {
        Map<String, String> baseEnv = new HashMap<>(Map.of(
                "ACCESS_LOGGING", "false",
                "GLOBAL_legacy.transform.service.enabled", "false",
                "GLOBAL_local.transform.service.enabled", "false",
                "JSON_LOGGING", String.valueOf(jsonLogging)
        ));
        baseEnv.putAll(env);
        alfContainer
                .withExposedPorts(8080)
                .withEnv(baseEnv);
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

    private boolean containsSpringDebugLog(String logs) {
        return logs.lines().anyMatch(line -> {
           return line.contains("DEBUG org.springframework");
        });
    }

    @Test
    public void testNonJsonLogging() {
        try (GenericContainer<?> alfContainer = new GenericContainer<>(getAlfrescoImageName())) {
            setupAlfrescoTestContainer(alfContainer, false, Map.of());
            alfContainer.start();

            // Let the logs accumulate
            Awaitility.await().until(() -> alfContainer.getLogs().length() > MIN_LOG_LINES);

            String logs = alfContainer.getLogs();

            Assert.assertFalse("Logs should not be json. Logs:\n" + logs, isJsonLogs(logs));
        }
    }

    @Test
    public void testJsonLogging() {
        try (GenericContainer<?> alfContainer = new GenericContainer<>(getAlfrescoImageName())) {
            setupAlfrescoTestContainer(alfContainer, true, Map.of());
            alfContainer.start();

            // Let the logs accumulate
            Awaitility.await().until(() -> alfContainer.getLogs().length() > MIN_LOG_LINES);

            String logs = alfContainer.getLogs();

            // Every line should be json
            Assert.assertTrue("Logs should be json. Logs:\n" + logs, isJsonLogs(logs));
            Stream<JsonNode> jsonNodes = getJsonLogs(logs);
            Assert.assertTrue(validateApplicationJsonLog(jsonNodes));
        }
    }

    @Test
    public void testLogLevelConfiguration() {
        try (GenericContainer<?> alfContainer = new GenericContainer<>(getAlfrescoImageName())) {
            // Start container with debug Spring
            setupAlfrescoTestContainer(alfContainer, false, Map.of("LOG_LEVEL_org_springframework", "debug"));
            alfContainer.start();

            // Accumulate some logs (make sure we get enough to catch some Spring logs)
            Awaitility.await().timeout(Duration.ofMinutes(1)).until(() -> alfContainer.getLogs().lines().count() > 50);

            String logs = alfContainer.getLogs();

            // Now find log entries from Spring marked as Debug
            Assert.assertTrue(containsSpringDebugLog(logs));
        }
    }
}
