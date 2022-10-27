package eu.xenit.alfresco.tomcat.embedded.share.config;


import eu.xenit.alfresco.tomcat.embedded.config.Configuration;

import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

public class EnvironmentVariableShareConfigurationProvider implements ShareConfigurationProvider {

    private static final String ALFRESCO_HOST = "ALFRESCO_HOST";
    private static final String ALFRESCO_PORT = "ALFRESCO_PORT";
    private static final String ALFRESCO_PROTOCOL = "ALFRESCO_PROTOCOL";
    private static final String ALFRESCO_CONTEXT = "ALFRESCO_CONTEXT";

    private static final String ALFRESCO_INTERNAL_HOST = "ALFRESCO_INTERNAL_HOST";
    private static final String ALFRESCO_INTERNAL_PORT = "ALFRESCO_INTERNAL_PORT";
    private static final String ALFRESCO_INTERNAL_PROTOCOL = "ALFRESCO_INTERNAL_PROTOCOL";
    private static final String ALFRESCO_INTERNAL_CONTEXT = "ALFRESCO_INTERNAL_CONTEXT";


    @Override
    public ShareConfiguration getConfiguration(Configuration baseConfiguration) {
        ShareConfiguration baseShareConfiguration = (ShareConfiguration) baseConfiguration;
        setPropertyFromEnv(ALFRESCO_HOST, baseShareConfiguration::setAlfrescoHost);
        setPropertyFromEnv(ALFRESCO_PORT, value -> baseShareConfiguration.setAlfrescoPort(Integer.parseInt(value)));
        setPropertyFromEnv(ALFRESCO_PROTOCOL, baseShareConfiguration::setAlfrescoProtocol);
        setPropertyFromEnv(ALFRESCO_CONTEXT, baseShareConfiguration::setAlfrescoContext);
        setPropertyFromEnv(ALFRESCO_INTERNAL_HOST, baseShareConfiguration::setAlfrescoInternalHost);
        setPropertyFromEnv(ALFRESCO_INTERNAL_PORT, value -> baseShareConfiguration.setAlfrescoInternalPort(Integer.parseInt(value)));
        setPropertyFromEnv(ALFRESCO_INTERNAL_PROTOCOL, baseShareConfiguration::setAlfrescoInternalProtocol);
        setPropertyFromEnv(ALFRESCO_INTERNAL_CONTEXT, baseShareConfiguration::setAlfrescoInternalContext);
        return baseShareConfiguration;
    }


}
