package eu.xenit.alfresco.tomcat.embedded;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HealthCheckHelperTest {

    @Test
    void testFailingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck(HealthCheck.ALFRESCO_DEFAULT_LIVE_PROBE, 100, 200);
        Assertions.assertNotEquals(0, status, "Failing healthcheck should not have status 0");

        status = HealthCheck.doHealthCheck("https://xenit.eu", 2000, 500);
        Assertions.assertNotEquals(0, status, "Failing healthcheck should not have status 0");
    }

    @Test
    void testSucceedingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck("https://xenit.eu", 2000, 200);
        Assertions.assertEquals(0, status);
    }

}