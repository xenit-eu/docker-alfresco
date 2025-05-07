package eu.xenit.docker.alfresco.test;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class LoggingTests {


    public static final long WAIT_FOR_LOGS_DUR = Duration.ofSeconds(10).toMillis();
    final Set<String> REQ_COMMON_LOGGING_FIELDS = Set.of("timestamp", "type");

    final Set<String> REQ_APPLICATION_LOGGING_FIELDS = union(
            Set.of("loggerName", "severity", "thread", "shortMessage", "fullMessage"), REQ_COMMON_LOGGING_FIELDS);


    private static <E> Set<E> union(Set<E> specificFields, Set<E> commonFields) {
        Set<E> result = new HashSet<>(specificFields);
        result.addAll(commonFields);
        return result;
    }

    private boolean isCorrectJsonLogs(String logs) {
        String applicationSample = logs.lines().filter(line -> line.contains("\"type\":\"application\"")).findFirst().orElse("");
        return hasEntries(applicationSample, REQ_APPLICATION_LOGGING_FIELDS);
    }

    private boolean hasEntries(String logSample, Set<String> fields) {
        assert logSample != null;
        for (String entry : fields) {
            if (!logSample.contains(entry)) {
                return false;
            }
        }
        return true;
    }

    private void alfrescoSetup(GenericContainer<?> alfContainer, boolean jsonLogging) {
        alfContainer.withExposedPorts(8080);
        alfContainer.withEnv(Map.of(
                "ACCESS_LOGGING", "false",
                "GLOBAL_legacy.transform.service.enabled", "false",
                "GLOBAL_local.transform.service.enabled", "false"
        ));
        if (jsonLogging) {
            alfContainer.withEnv("JSON_LOGGING", "true");
        } else {
            alfContainer.withEnv("JSON_LOGGING", "false");
        }
    }

    @Test
    public void testNonJsonLogging() throws InterruptedException {
        try (GenericContainer<?> alfresco = new GenericContainer<>(
                DockerImageName.parse(System.getProperty("alfresco_image_name")))) {
            alfrescoSetup(alfresco, false);
            alfresco.start();

            // Sleep to let the logs accumulate
            Thread.sleep(WAIT_FOR_LOGS_DUR);

            String logs = alfresco.getLogs();
            Assert.assertFalse("Logs should not be json", isCorrectJsonLogs(logs));
        }
    }

    @Test
    public void testJsonLogging() throws InterruptedException {
        try (GenericContainer<?> alfresco = new GenericContainer<>(
                DockerImageName.parse(System.getProperty("alfresco_image_name")))) {
            alfrescoSetup(alfresco, true);
            alfresco.start();

            // Sleep to let the logs accumulate
            Thread.sleep(WAIT_FOR_LOGS_DUR);

            String logs = alfresco.getLogs();
            Assert.assertTrue("Logs do not contain required fields or aren't json", isCorrectJsonLogs(logs));
        }
    }
}
