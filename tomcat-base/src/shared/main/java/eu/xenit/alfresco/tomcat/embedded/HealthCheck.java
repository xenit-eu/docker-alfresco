package eu.xenit.alfresco.tomcat.embedded;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class HealthCheck {

    public static final String ALFRESCO_DEFAULT_LIVE_PROBE = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/probes/-live-";
    public static final int DEFAULT_TIMEOUT = 2000;
    public static final int DEFAULT_STATUS_CODE = 200;

    public static void main(String[] args) throws IOException, InterruptedException {
        String healthEndpoint = ALFRESCO_DEFAULT_LIVE_PROBE;
        if (args.length > 0) {
            healthEndpoint = args[0];
        }
        int timeout = DEFAULT_TIMEOUT;
        if (args.length > 1) {
            timeout = Integer.parseInt(args[1]);
        }
        int statusCode = DEFAULT_STATUS_CODE;
        if (args.length > 2) {
            statusCode = Integer.parseInt(args[2]);
        }

        System.exit(doHealthCheck(healthEndpoint, timeout, statusCode));
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
