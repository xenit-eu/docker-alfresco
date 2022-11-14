package eu.xenit.alfresco.tomcat.embedded.healthcheck;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariableConfigurationProvider;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Objects;

public class HealthCheck {
    public static final String ALFRESCO_DEFAULT_LIVE_PROBE = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/probes/-live-";
    public static final String SHARE_DEFAULT_LIVE_PROBE = "http://localhost:8080/share";

    public static final int DEFAULT_TIMEOUT = 2000;
    public static final int DEFAULT_STATUS_CODE = 200;

    private HealthCheck() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TomcatConfiguration configuration = new EnvironmentVariableConfigurationProvider()
                .getConfiguration(new DefaultConfigurationProvider()
                        .getConfiguration());
        var exitCode = 0;
        if (configuration.isAlfrescoEnabled()) {
            exitCode = healthCheck(ALFRESCO_DEFAULT_LIVE_PROBE, 0, args);
        }
        if (configuration.isShareEnabled()) {
            exitCode = healthCheck(SHARE_DEFAULT_LIVE_PROBE, 302, args);
        }
        System.exit(exitCode);
    }

    private static int healthCheck(String endpoint, int statusCode, String[] args) throws IOException, InterruptedException {
        var spec = HealthCheck.setupHealthCheck(endpoint, statusCode, args);
        var exitCode = doHealthCheck(spec);
        if(exitCode != 0) {
            System.exit(exitCode);
        }
        return exitCode;
    }

    public static HealthCheckSpec setupHealthCheck(String defaultLiveProbe, int statusCode, String... args) {
        String healthEndpoint = defaultLiveProbe;
        if (args.length > 0) {
            healthEndpoint = args[0];
        }
        int timeoutArg = DEFAULT_TIMEOUT;
        if (args.length > 1) {
            timeoutArg = Integer.parseInt(args[1]);
        }
        int statusCodeArg = statusCode == 0 ? DEFAULT_STATUS_CODE : statusCode;
        if (args.length > 2) {
            statusCodeArg = Integer.parseInt(args[2]);
        }

        return new HealthCheckSpec(healthEndpoint, timeoutArg, statusCodeArg);
    }

    public static int doHealthCheck(HealthCheckSpec spec)
            throws IOException, InterruptedException {
        var client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(spec.getTimeOut()))
                .build();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(spec.getEndPoint()))
                .build();
        try {
            var response = client.send(httpRequest, BodyHandlers.ofString());
            if (response.statusCode() == spec.getStatusCode()) {
                return 0;
            }
            return 1;
        } catch (ConnectException e) {
            return 2;
        } catch (HttpConnectTimeoutException e) {
            return 3;
        }
    }

}
