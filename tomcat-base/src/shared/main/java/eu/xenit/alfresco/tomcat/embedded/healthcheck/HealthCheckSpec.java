package eu.xenit.alfresco.tomcat.embedded.healthcheck;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class HealthCheckSpec {

    @NonNull private String endPoint;
    private int timeOut;
    private int statusCode;

}
