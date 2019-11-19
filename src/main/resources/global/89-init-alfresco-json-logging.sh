#!/bin/bash

set -e

echo "Alfresco json logging init start"

if [[ $ALFRESCO_JSON_LOGGING == true ]]
then
    JSON_LOGGING_VERSION=${JSON_LOGGING_VERSION:-'0.0.3'}
    # simulate the deployment of the amp from https://github.com/xenit-eu/alfred-json-logging
    # assumes the necessary jars are present in /docker-config/dependencies
    cp /docker-config/dependencies/* /usr/local/tomcat/webapps/alfresco/WEB-INF/lib/

    mkdir -p /usr/local/tomcat/webapps/alfresco/WEB-INF/classes/alfresco/module/alfred-json-logging/
    echo "<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE beans PUBLIC '-//SPRING//DTD BEAN//EN' 'http://www.springframework.org/dtd/spring-beans.dtd'>
<!-- Workaround for MNT-18557: force the log4j hierarchy to be initialized as early as possible. -->
<beans>
  <bean id=\"eu.xenit.alfred.logging.NullBeanDefinitionRegistryPostProcessor\" class=\"eu.xenit.alfred.logging.NullBeanDefinitionRegistryPostProcessor\" depends-on=\"log4JHierarchyInit\"/>
</beans>" >/usr/local/tomcat/webapps/alfresco/WEB-INF/classes/alfresco/module/alfred-json-logging/module-context.xml
    echo "module.version=${JSON_LOGGING_VERSION}
module.description=Alfresco json logging
module.id=alfred-json-logging
module.repo.version.max=999
module.title=alfred-json-logging
module.repo.version.min=0
module.installState=INSTALLED
module.installDate="`date +"%FT%T.%3N%:z" |sed 's/:/\\\\:/g'` >/usr/local/tomcat/webapps/alfresco/WEB-INF/classes/alfresco/module/alfred-json-logging/module.properties

    mkdir -p /usr/local/tomcat/webapps/alfresco/WEB-INF/classes/alfresco/extension
    echo "# Set root logger level to error
log4j.rootLogger=error, Console

###### Console appender definition #######

# All outputs currently set to be a ConsoleAppender.
log4j.appender.Console=org.apache.log4j.ConsoleAppender
#log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout=net.logstash.log4j.JSONEventLayoutV1

# use log4j NDC to replace %x with tenant domain / username
log4j.appender.Console.layout.ConversionPattern=%d{ISO8601} %x %-5p [%c{3}] [%t] %m%n" >/usr/local/tomcat/webapps/alfresco/WEB-INF/classes/alfresco/extension/alfred-json-logging-log4j.properties
fi

echo "Alfresco json logging init done"
