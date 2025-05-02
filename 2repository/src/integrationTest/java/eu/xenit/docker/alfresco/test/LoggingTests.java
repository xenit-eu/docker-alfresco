package eu.xenit.docker.alfresco.test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import uk.org.webcompere.systemstubs.rules.EnvironmentVariablesRule;

public class LoggingTests {

    final Set<String> REQ_COMMON_LOGGING_FIELDS = Set.of("timestamp", "type");

    final Set<String> REQ_APPLICATION_LOGGING_FIELDS = union(
            Set.of("loggerName", "severity", "thread"), REQ_COMMON_LOGGING_FIELDS);

    final Set<String> REQ_ACCESS_LOGGING_FIELDS = union(
            Set.of("requestTime", "responseStatus", "requestMethod", "requestUri"),
            REQ_COMMON_LOGGING_FIELDS);

    private static <E> Set<E> union(Set<E> specificFields, Set<E> commonFields) {
        Set<E> result = new HashSet<>(specificFields);
        result.addAll(commonFields);
        return result;
    }
    //TODO: Sql logs?

    @Rule
    public EnvironmentVariablesRule environmentVariablesRule = new EnvironmentVariablesRule();

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
    private void checkJsonLogs(String logs){
        // TODO: json parsing?
        Assert.fail();

        String accessSample = logs.lines().filter(line -> line.contains("\"type\":\"access\"")).findFirst().orElseThrow(AssertionError::new);
        for(String entry : REQ_ACCESS_LOGGING_FIELDS){
            Assert.assertTrue("Access logs missing field: " + entry, accessSample.contains(entry));
        }
        String applicationSample = logs.lines().filter(line -> line.contains("\"type\":\"application\"")).findFirst().orElseThrow(AssertionError::new);
        for(String entry : REQ_APPLICATION_LOGGING_FIELDS){
            Assert.assertTrue("Application logs missing field: " + entry, applicationSample.contains(entry));
        }
    }

    private static String getContainerLogs(DockerClient dockerClient, Container container)
            throws InterruptedException {
        StringBuilder logs = new StringBuilder();
        ResultCallback.Adapter<Frame> callback = new ResultCallback.Adapter<>() {
            public void onNext(Frame frame) {
                logs.append(new String(frame.getPayload()));
            }
        };
        // Might need to limit the amount of log lines retrieved if logs are too big using withTail(n)
        dockerClient.logContainerCmd(container.getId()).withStdOut(true).withStdErr(true).exec(callback)
                .awaitCompletion();
        return logs.toString();
    }

    @Test
    public void testJsonLogging() {
        final String containerName = "alfresco";
        try (DockerClient dockerClient = createDockerClient()) {
            List<Container> containers = dockerClient.listContainersCmd().withStatusFilter(List.of("running")).exec();
            Container container = findContainerByName(containerName, containers);
            assert container != null;

            String logs = getContainerLogs(dockerClient, container);
            Assert.assertFalse(logs.isEmpty());
            checkJsonLogs(logs);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.fail(); // TODO: with and without json logging
    }


}
