package eu.xenit.alfresco.tomcat.embedded;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthCheckTest {

    @Test
    public void testFailingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck(HealthCheck.ALFRESCO_DEFAULT_LIVE_PROBE, 100, 200);
        Assertions.assertNotEquals(0, status, "Failing healthcheck should not have status 0");
    }

    @Test
    public void testSucceedingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck("https://xenit.eu", 2000, 200);
        Assertions.assertEquals(0, status);
    }

}