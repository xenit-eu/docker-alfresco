package eu.xenit.alfresco.tomcat.embedded.healthcheck;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthCheckTest {

    @Test
    void testFailingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck(new HealthCheckSpec(HealthCheck.ALFRESCO_DEFAULT_LIVE_PROBE, 100, 200));
        Assertions.assertNotEquals(0, status, "Failing healthcheck should not have status 0");

        status = HealthCheck.doHealthCheck(new HealthCheckSpec("https://xenit.eu", 2000, 500));
        Assertions.assertNotEquals(0, status, "Failing healthcheck should not have status 0");
    }

    @Test
    void testSucceedingHealthCheck() throws IOException, InterruptedException {
        int status = HealthCheck.doHealthCheck(new HealthCheckSpec("https://xenit.eu", 2000, 200));
        Assertions.assertEquals(0, status);
    }

    @Test
    void testSetupHealthCheck() {
        var spec = HealthCheck.setupHealthCheck(HealthCheck.ALFRESCO_DEFAULT_LIVE_PROBE, 200);
        assertHealthCheckSpec(spec, HealthCheck.ALFRESCO_DEFAULT_LIVE_PROBE, HealthCheck.DEFAULT_TIMEOUT, 200);

        var endpoint = "http://testcheck";
        var timeout = 654;
        var statusCode = 500;
        spec = HealthCheck.setupHealthCheck(HealthCheck.SHARE_DEFAULT_LIVE_PROBE, 302, endpoint,
                Integer.toString(timeout), Integer.toString(statusCode));
        assertHealthCheckSpec(spec, endpoint, timeout, statusCode);

    }

    private static void assertHealthCheckSpec(HealthCheckSpec spec, String endpoint, int timeout, int statusCode) {
        Assertions.assertEquals(endpoint, spec.getEndPoint());
        Assertions.assertEquals(timeout, spec.getTimeOut());
        Assertions.assertEquals(statusCode, spec.getStatusCode());
    }

}