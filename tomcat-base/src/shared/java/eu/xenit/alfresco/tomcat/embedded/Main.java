package eu.xenit.alfresco.tomcat.embedded;

import biz.paluch.logging.gelf.jul.GelfFormatter;
import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariableConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
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

            setGlobalProperties(configuration);

            TomcatFactory tomcatFactory = new TomcatFactory(configuration);
            Tomcat tomcat = tomcatFactory.getTomcat();
            //Needs to be done after
            if(configuration.isJsonLogging()) {
                configureJsonLogging();
            }
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Main method failed", e);
            System.exit(1);
        }
    }

    private static void setGlobalProperties(Configuration configuration) {
        configuration.getGlobalProperties().forEach((key, value) -> System.setProperty(key, value));
    }

    private static void configureJsonLogging() {
        configureLoggerToJSONStdOut(Logger.getLogger(""), "tomcat");
        configureLoggerToJSONStdOut(LOG.getParent(), "tomcat");
        for (Handler handler : LOG.getHandlers()) {
            LOG.removeHandler(handler);
        }
    }

    private static void configureLoggerToJSONStdOut(Logger logger, String type) {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        GelfFormatter formatter = new GelfFormatter();
        formatter.setTimestampPattern("yyyy-MM-dd HH:mm:ss,SSS");
        formatter.setFields("Severity, Time, LoggerName");
        formatter.setAdditionalFields("type="+type);
        formatter.setExtractStackTrace("true");
        formatter.setFilterStackTrace(true);

        Handler customHandler = new ConsoleHandler() {
            {
                setOutputStream(System.out);
            }
        };
        customHandler.setFormatter(formatter);
        logger.addHandler(customHandler);
    }

}
