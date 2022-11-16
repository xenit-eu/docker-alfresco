package eu.xenit.alfresco.tomcat.embedded.share.config;


import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_CONTEXT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_HOST;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_INTERNAL_PROTOCOL;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PORT;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.ALFRESCO_PROTOCOL;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.SHARE_CONFIG_PATH;
import static eu.xenit.alfresco.tomcat.embedded.share.config.ShareEnvironmentVariables.SHARE_CONFIG_TEMPLATE_FILE;
import static eu.xenit.alfresco.tomcat.embedded.utils.ConfigurationHelper.setPropertyFromEnv;

public class EnvironmentVariableShareConfigurationProvider implements ShareConfigurationProvider {


    @Override
    public ShareConfiguration getConfiguration(ShareConfiguration baseShareConfiguration) {
        setPropertyFromEnv(ALFRESCO_HOST, baseShareConfiguration::setAlfrescoHost);
        setPropertyFromEnv(ALFRESCO_PORT, value -> baseShareConfiguration.setAlfrescoPort(Integer.parseInt(value)));
        setPropertyFromEnv(ALFRESCO_PROTOCOL, baseShareConfiguration::setAlfrescoProtocol);
        setPropertyFromEnv(ALFRESCO_CONTEXT, baseShareConfiguration::setAlfrescoContext);
        setPropertyFromEnv(ALFRESCO_INTERNAL_HOST, baseShareConfiguration::setAlfrescoInternalHost);
        setPropertyFromEnv(ALFRESCO_INTERNAL_PORT, value -> baseShareConfiguration.setAlfrescoInternalPort(Integer.parseInt(value)));
        setPropertyFromEnv(ALFRESCO_INTERNAL_PROTOCOL, baseShareConfiguration::setAlfrescoInternalProtocol);
        setPropertyFromEnv(ALFRESCO_INTERNAL_CONTEXT, baseShareConfiguration::setAlfrescoInternalContext);
        setPropertyFromEnv(SHARE_CONFIG_PATH, baseShareConfiguration::setShareConfigPath);
        setPropertyFromEnv(SHARE_CONFIG_TEMPLATE_FILE, baseShareConfiguration::setShareConfigTemplateFile);
        return baseShareConfiguration;
    }


}
