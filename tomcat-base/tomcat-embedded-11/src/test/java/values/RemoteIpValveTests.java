package valves;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
import io.restassured.RestAssured;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;

class RemoteIpValveTests {
    @Test
    void testRemoteIpValveHeadersConfig() throws IOException, LifecycleException {
        TomcatConfiguration configuration = new DefaultConfigurationProvider().getConfiguration();
        configuration.setTomcatBaseDir("tomcat-base");
        configuration.setTomcatPort(0);
        TomcatFactory tomcatFactory = new TomcatFactory(configuration);
        Tomcat tomcat = tomcatFactory.getTomcat();

        Context context = tomcat.addContext("/", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "testServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                String scheme = req.getScheme();
                String serverName = req.getServerName();
                int serverPort = req.getServerPort();
                String remoteHost = req.getRemoteHost();
                String remoteAddr = req.getRemoteAddr();

                String jsonResponseString = String.format(
                        "{\"request.scheme\":\"%s\",\"request.serverName\":\"%s\",\"request.serverPort\":%d,\"request.remoteHost\":\"%s\",\"request.remoteAddr\":\"%s\"}",
                        scheme, serverName, serverPort, remoteHost, remoteAddr
                );

                resp.setContentType("application/json; charset=UTF-8");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(jsonResponseString);
            }
        });
        context.addServletMappingDecoded("/test", "testServlet");

        try {
            tomcat.start();
            RestAssured.given()
                .baseUri("http://localhost")
                .port(tomcat.getConnector().getLocalPort())
                .log().method()
                .log().uri()
                .log().headers()
                .header("X-Forwarded-For", "81.82.199.31")
                .header("X-Forwarded-Proto", "https")
                .header("Host", "test.host.eu")
                .header("X-Forwarded-Port", 443)
                .when()
                .get("/test")
                .then()
                .log().status()
                .log().ifValidationFails()
                .log().body(false)
                .statusCode(200)
                .body("'request.scheme'", equalTo("https"))
                .body("'request.serverName'", equalTo("test.host.eu"))
                .body("'request.serverPort'", equalTo(443))
                .body("'request.remoteHost'", equalTo("81.82.199.31"))
                .body("'request.remoteAddr'", equalTo("81.82.199.31"));
        } finally {
            tomcat.stop();
            tomcat.destroy();
        }
    }
}
