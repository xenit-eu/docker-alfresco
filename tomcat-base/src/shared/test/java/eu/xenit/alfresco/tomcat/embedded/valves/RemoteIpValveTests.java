package eu.xenit.alfresco.tomcat.embedded.valves;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoteIpValveTests {
    @Test
    void testRemoteIpValveHeadersConfig() throws IOException, LifecycleException {
        TomcatConfiguration configuration = new DefaultConfigurationProvider().getConfiguration();
        TomcatFactory tomcatFactory = new TomcatFactory(configuration);
        Tomcat tomcat = tomcatFactory.getTomcat();

        Context context = tomcat.addContext("/", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "testServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                String scheme = req.getScheme();
                assertEquals("https", scheme);
                String serverName = req.getServerName();
                assertEquals("test.host.eu", serverName);
                int serverPort = req.getServerPort();
                assertEquals(443, serverPort);
                String remoteHost = req.getRemoteHost();
                assertEquals("81.82.199.31", remoteHost);
                String remoteAddr = req.getRemoteAddr();
                assertEquals("81.82.199.31", remoteAddr);

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

        tomcat.start();

        RestAssured.defaultParser = Parser.JSON;
        String responseString = RestAssured.given()
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
                .extract().asString();

        System.out.println("Raw Response Body: " + responseString);
    }
}
