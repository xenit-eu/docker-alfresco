package eu.xenit.alfresco.tomcat.embedded;

import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariableConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
import eu.xenit.json.jul.JsonFormatter;
import org.apache.catalina.startup.Tomcat;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Configuration configuration = new DefaultConfigurationProvider().getConfiguration();
            configuration = new EnvironmentVariableConfigurationProvider().getConfiguration(configuration);

            setSystemProperties(configuration);

            TomcatFactory tomcatFactory = new TomcatFactory(configuration);
            Tomcat tomcat = tomcatFactory.getTomcat();
            //Needs to be done after
            configureLogging(configuration.isJsonLogging());
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Main method failed", e);
            System.exit(1);
        }
    }

    private static void setSystemProperties(Configuration configuration) {
        configuration.getSystemProperties().forEach((key, value) -> {
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        });
    }

    private static void configureLogging(boolean json) {
        configureLoggerToJSONStdOut(Logger.getLogger(""), "tomcat", json);
        configureLoggerToJSONStdOut(LOG.getParent(), "tomcat", json);
        for (Handler handler : LOG.getHandlers()) {
            LOG.removeHandler(handler);
        }
    }

    private static void configureLoggerToJSONStdOut(Logger logger, String component, boolean json) {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        //Sonar complains about the following block, but I don't know how to do it in a nicer way.
        Handler customHandler = new ConsoleHandler() {
            {
                setOutputStream(System.out);
            }
        };

        if (json) {
            JsonFormatter formatter = new JsonFormatter();
            formatter.setType("application");
            formatter.setComponent(component);
            formatter.setExtractStackTrace("true");
            formatter.setFilterStackTrace(true);
            customHandler.setFormatter(formatter);
        }

        logger.addHandler(customHandler);
    }

}
