package eu.xenit.docker.alfresco.test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTests {

    private static final Logger log = LoggerFactory.getLogger(LoggingTests.class);
    final Set<String> REQ_COMMON_LOGGING_FIELDS = Set.of("timestamp", "type");

    final Set<String> REQ_APPLICATION_LOGGING_FIELDS = union(
            Set.of("loggerName", "severity", "thread", "shortMessage", "fullMessage"), REQ_COMMON_LOGGING_FIELDS);

    final Set<String> REQ_ACCESS_LOGGING_FIELDS = union(
            Set.of("requestTime", "responseStatus", "requestMethod", "requestUri"),
            REQ_COMMON_LOGGING_FIELDS);

    private static <E> Set<E> union(Set<E> specificFields, Set<E> commonFields) {
        Set<E> result = new HashSet<>(specificFields);
        result.addAll(commonFields);
        return result;
    }

    private Container findContainerByName(String value, List<Container> containers) {
        assert value != null && containers != null;
        for (Container container : containers) {
            for (String name : container.getNames()) {
                if (name != null && name.contains(value)) {
                    return container;
                }
            }
        }
        throw new AssertionError("No running docker container running for name: " + value);
    }


    private DockerClient createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig()).maxConnections(100).connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45)).build();
        return DockerClientImpl.getInstance(config, httpClient);
    }

    private boolean isCorrectJsonLogs(String logs) {
        // This fully relies on the logs containing one of each log type before this test runs.
        String accessSample = logs.lines().filter(line -> line.contains("\"type\":\"access\"")).findFirst().orElse("");
        String applicationSample = logs.lines().filter(line -> line.contains("\"type\":\"application\"")).findFirst().orElse("");
        return hasEntries(accessSample, REQ_ACCESS_LOGGING_FIELDS)
                && hasEntries(applicationSample, REQ_APPLICATION_LOGGING_FIELDS);
    }

    private boolean hasEntries(String accessSample, Set<String> fields) {
        assert accessSample != null;
        for (String entry : fields) {
            if (!accessSample.contains(entry)) {
                return false;
            }
        }
        return true;
    }

    private static String getContainerLogs(DockerClient dockerClient, String containerId)
            throws InterruptedException {
        StringBuilder logs = new StringBuilder();
        ResultCallback.Adapter<Frame> callback = new ResultCallback.Adapter<>() {
            public void onNext(Frame frame) {
                logs.append(new String(frame.getPayload()));
            }
        };
        // Might need to limit the amount of log lines retrieved if logs are too big using withTail(n)
        dockerClient.logContainerCmd(containerId).withStdOut(true).withStdErr(true).exec(callback)
                .awaitCompletion();
        return logs.toString();
    }

    @Test
    public void testJsonLogging() throws IOException, InterruptedException {

        final String containerName = "alfresco";
        try (DockerClient dockerClient = createDockerClient()) {
            List<Container> containers = dockerClient.listContainersCmd().withStatusFilter(List.of("running")).exec();
            Container container = findContainerByName(containerName, containers);
            assert container != null;
            System.out.println(
                    Arrays.toString(dockerClient.inspectContainerCmd(container.getId()).exec().getConfig().getEnv()));
            String logs = getContainerLogs(dockerClient, container.getId());
            Assert.assertFalse(logs.isEmpty());
            Assert.assertTrue("Logs do not contain required fields or aren't json", isCorrectJsonLogs(logs));
        }
    }

    @Test
    public void testNonJsonLogging() throws IOException, InterruptedException {

        final String containerName = "alfresco";
        try (DockerClient dockerClient = createDockerClient()) {
            List<Container> containers = dockerClient.listContainersCmd().withStatusFilter(List.of("running")).exec();
            Container container = findContainerByName(containerName, containers);
            assert container != null;
            String containerId = container.getId();
            System.out.println(
                    Arrays.toString(dockerClient.inspectContainerCmd(containerId).exec().getConfig().getEnv()));
            InspectContainerResponse inspectResp = dockerClient.inspectContainerCmd(containerId).exec();

            // Change envVars
            ContainerConfig config = inspectResp.getConfig();
            List<String> envVars = new ArrayList<>(
                    config.getEnv() != null ? List.of(config.getEnv()) : Collections.emptyList());
            envVars.removeIf(s -> s.startsWith("JSON_LOGGING="));
            envVars.add("JSON_LOGGING=false");

            // Create new container
            String newContainerId = dockerClient.createContainerCmd(config.getImage())
                    .withName("no_json_container")
                    .withEnv(envVars)
                    .withCmd(config.getCmd())
                    .withEntrypoint(config.getEntrypoint())
                    .withHostConfig(inspectResp.getHostConfig())
                    .exec()
                    .getId();

            // Start new container
            dockerClient.startContainerCmd(newContainerId).exec();
            Thread.sleep(10_000);
            InspectContainerResponse newInspectResp = dockerClient.inspectContainerCmd(newContainerId).exec();

            // Check logs of new container
            String logs = getContainerLogs(dockerClient, newInspectResp.getId());
            Assert.assertFalse(logs.isEmpty());
            Assert.assertFalse("Logs should not be json logs", isCorrectJsonLogs(logs));
            dockerClient.stopContainerCmd(newContainerId).withTimeout(5).exec();
            dockerClient.removeContainerCmd(newContainerId).exec();
            assert false;
        }
    }
}
