package eu.xenit.alfresco.tomcat.embedded.tomcat;

import eu.xenit.alfresco.tomcat.embedded.config.TomcatConfiguration;
import org.apache.catalina.startup.Tomcat;

public interface TomcatCustomizer {

    void customize(Tomcat tomcat, TomcatConfiguration configuration);

}
