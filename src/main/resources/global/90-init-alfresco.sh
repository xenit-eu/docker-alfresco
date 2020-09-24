#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -e

echo "Alfresco init start"

export DB_HOST=${DB_HOST:-'postgresql'}
export DB_PORT=${DB_PORT:-'5432'}
export DB_NAME=${DB_NAME:-'alfresco'}
export SOLR_SSL=${SOLR_SSL:-'https'}
export JAVA_OPTS

CONFIG_FILE=${CONFIG_FILE:-${CATALINA_HOME}'/shared/classes/alfresco-global.properties'}
LOG4J_CONFIG_FILE=${LOG4J_CONFIG_FILE:-${CATALINA_HOME}'/webapps/alfresco/WEB-INF/classes/alfresco/extension/dev-log4j.properties'}
TOMCAT_CONFIG_FILE=${CATALINA_HOME}'/bin/setenv.sh'
TOMCAT_SERVER_FILE=${CATALINA_HOME}'/conf/server.xml'

${JAVA_HOME}/bin/java -jar /90-init-alfresco.jar "$CONFIG_FILE" "$TOMCAT_CONFIG_FILE" "$LOG4J_CONFIG_FILE"

if [[ $SOLR_SSL == none ]] && [[ $ALFRESCO_VERSION != "5.0"* ]] && [[ $ALFRESCO_VERSION != "3"* ]] && [[ $ALFRESCO_VERSION != "4"* ]]; then
  #remove the SSL connector
  xmlstarlet edit --inplace --delete "/Server/Service[@name='Catalina']/Connector[@SSLEnabled='true' and @port='\${TOMCAT_PORT_SSL}']" $TOMCAT_SERVER_FILE
fi

if [ -n "$CATALINA_HOME" ]; then

  user="tomcat"
  # Check existence of alf_data/contentstore
  # -> if dir exists, check if the permissions are incorrect
  # --> if permissions are incorrect, exit with error code
  # --> else if correct, do nothing
  # -> else if not exists, chown only alf_data
  if [[ -e /opt/alfresco/alf_data/contentstore && -e /opt/alfresco/alf_data/contentstore.deleted ]]; then
    if [[ ($(stat -c %U /opt/alfresco/alf_data/contentstore) != "$user" && $(stat -c %a /opt/alfresco/alf_data/contentstore) -lt 766)\
     || ($(stat -c %U /opt/alfresco/alf_data/contentstore.deleted) != "$user" && $(stat -c %a /opt/alfresco/alf_data/contentstore.deleted) -lt 766) ]]; then
      # custom exit code for debug to this script
      echo "Contentstores exists within /opt/alfresco/alf_data , but do not have correct ownership/permission to run alfresco. Exiting with code 64."
      exit 64
    fi
  else
    echo "No contentstore in alf_data ; assuming dev/test environment, chowning alf_data to tomcat user."
    chown $user:$user /opt/alfresco/alf_data
  fi

  if [[ $(stat -c %U "$CATALINA_HOME/temp") != "$user" ]]; then
    chown -R $user:$user "$CATALINA_HOME"/temp
  fi

fi

echo "Alfresco init done"
