package eu.xenit.alfresco.tomcat.embedded;

import eu.xenit.alfresco.tomcat.embedded.alfresco.config.AlfrescoConfiguration;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.DefaultAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.alfresco.config.EnvironmentVariableAlfrescoConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat.AlfrescoTomcatFactoryHelper;
import eu.xenit.alfresco.tomcat.embedded.config.Configuration;
import eu.xenit.alfresco.tomcat.embedded.config.DefaultConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.config.EnvironmentVariableConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.DefaultShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.EnvironmentVariableShareConfigurationProvider;
import eu.xenit.alfresco.tomcat.embedded.share.config.ShareConfiguration;
import eu.xenit.alfresco.tomcat.embedded.share.tomcat.ShareTomcatFactoryHelper;
import eu.xenit.alfresco.tomcat.embedded.tomcat.TomcatFactory;
import eu.xenit.json.jul.JsonFormatter;
import org.apache.catalina.startup.Tomcat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static eu.xenit.alfresco.tomcat.embedded.alfresco.tomcat.AlfrescoTomcatFactoryHelper.createGlobalPropertiesFile;


public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Configuration configuration = new EnvironmentVariableConfigurationProvider()
                    .getConfiguration(new DefaultConfigurationProvider()
                            .getConfiguration());
            Files.createDirectories(Paths.get(configuration.getGeneratedClasspathDir()));
            TomcatFactory tomcatFactory = new TomcatFactory(configuration);
            Tomcat tomcat = tomcatFactory.getTomcat();
            if (configuration.isAlfrescoEnabled()) {
                AlfrescoConfiguration
                        alfrescoConfiguration = new EnvironmentVariableAlfrescoConfigurationProvider()
                        .getConfiguration(new DefaultAlfrescoConfigurationProvider()
                                .getConfiguration(new AlfrescoConfiguration(configuration)));
                createGlobalPropertiesFile(alfrescoConfiguration);
                if (alfrescoConfiguration.isSolrSSLEnabled()) {
                    AlfrescoTomcatFactoryHelper.createSSLConnector(tomcat, alfrescoConfiguration);
                }
            }
            if (configuration.isShareEnabled()) {
                ShareConfiguration shareConfiguration = new EnvironmentVariableShareConfigurationProvider()
                        .getConfiguration(new DefaultShareConfigurationProvider()
                                .getConfiguration(new ShareConfiguration(configuration)));
                ShareTomcatFactoryHelper.createShareConfigCustomFile(shareConfiguration);
            }
            //Needs to be done after
            configureLogging(configuration.isJsonLogging());
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Main method failed", e);
            System.exit(1);
        }
    }


    public static void configureLogging(boolean json) {
        configureLoggerToJSONStdOut(Logger.getLogger(""), "tomcat", json);
        configureLoggerToJSONStdOut(LOG.getParent(), "tomcat", json);
        for (Handler handler : LOG.getHandlers()) {
            LOG.removeHandler(handler);
        }
    }

    public static void configureLoggerToJSONStdOut(Logger logger, String component, boolean json) {
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
