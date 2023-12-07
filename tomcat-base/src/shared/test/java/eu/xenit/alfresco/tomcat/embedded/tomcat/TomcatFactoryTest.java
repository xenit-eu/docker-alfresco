package eu.xenit.alfresco.tomcat.embedded.tomcat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Protocol;
import org.junit.jupiter.api.Test;

public class TomcatFactoryTest {

    @Test
    void testConnection() throws LifecycleException, IOException {
        TomcatConfiguration configuration = new DefaultConfigurationProvider().getConfiguration();
        String resourcePath = "emptywebapps";
        URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);

        if (resourceUrl == null) {
            throw new IllegalStateException("Resource not found: " + resourcePath);
        }

        // Convert URL to a File object and get the absolute path
        String webappsPath = new File(resourceUrl.getFile()).getAbsolutePath();
        configuration.setWebappsPath(webappsPath);
        TomcatFactory tomcatFactory = new TomcatFactory(configuration);
        Tomcat tomcat = tomcatFactory.getTomcat();
        try {
            tomcat.start();

            // URL pointing to your Tomcat server
            URL url = new URL("http://localhost:8080");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Check if response code is 404
            int responseCode = connection.getResponseCode();
            assertEquals(404, responseCode);

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Ensure Tomcat is stopped even if an exception occurs
            tomcat.stop();
        }
    }
}
