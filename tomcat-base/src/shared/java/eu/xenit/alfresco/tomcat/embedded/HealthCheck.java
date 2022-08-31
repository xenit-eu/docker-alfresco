package eu.xenit.alfresco.tomcat.embedded;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class HealthCheck {

    public static void main(String[] args) throws IOException, InterruptedException {
        String healthEndpoint = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/probes/-live-";
        if (args.length > 0) {
            healthEndpoint = args[0];
        }
        int timeout = 2000;
        if (args.length > 1) {
            timeout = Integer.parseInt(args[1]);
        }
        int statusCode = 200;
        if (args.length > 2) {
            statusCode = Integer.parseInt(args[2]);
        }

        var client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(timeout))
                .build();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(healthEndpoint))
                .build();
        var response = client.send(httpRequest, BodyHandlers.ofString());
        if (response.statusCode() == statusCode) {
            System.exit(0);
        }
        System.exit(1);
    }

}
