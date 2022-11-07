package eu.xenit.alfresco.tomcat.embedded;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
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
        Configuration configuration = new EnvironmentVariableConfigurationProvider()
                .getConfiguration(new DefaultConfigurationProvider()
                        .getConfiguration());
        if (configuration.isAlfrescoEnabled()) {
            HealthCheck.setupHealthCheck(ALFRESCO_DEFAULT_LIVE_PROBE, null, null, args);
        }
        if (configuration.isShareEnabled()) {
            HealthCheck.setupHealthCheck(SHARE_DEFAULT_LIVE_PROBE, null, 302, args);
        }
    }

    public static void setupHealthCheck(String defaultLiveProbe, Integer timeout, Integer statusCode, String[] args) throws IOException, InterruptedException {
        String healthEndpoint = defaultLiveProbe;
        if (args.length > 0) {
            healthEndpoint = args[0];
        }
        int timeoutArg = Objects.isNull(timeout) ? DEFAULT_TIMEOUT : timeout;
        if (args.length > 1) {
            timeoutArg = Integer.parseInt(args[1]);
        }
        int statusCodeArg = Objects.isNull(statusCode) ? DEFAULT_STATUS_CODE : statusCode;
        if (args.length > 2) {
            statusCodeArg = Integer.parseInt(args[2]);
        }

        System.exit(doHealthCheck(healthEndpoint, timeoutArg, statusCodeArg));
    }

    public static int doHealthCheck(String healthEndpoint, int timeout, int statusCode)
            throws IOException, InterruptedException {
        var client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(timeout))
                .build();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(healthEndpoint))
                .build();
        try {
            var response = client.send(httpRequest, BodyHandlers.ofString());
            if (response.statusCode() == statusCode) {
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
