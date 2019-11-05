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
TOMCAT_CONFIG_FILE=${CATALINA_HOME}'/bin/setenv.sh'
TOMCAT_SERVER_FILE=${CATALINA_HOME}'/conf/server.xml'

${JAVA_HOME}/bin/java -jar /90-init-alfresco.jar "$CONFIG_FILE" "$TOMCAT_CONFIG_FILE"

if [[ $SOLR_SSL == none ]] && [[ $ALFRESCO_VERSION != "5.0"* ]] && [[ $ALFRESCO_VERSION != "3"* ]] && [[ $ALFRESCO_VERSION != "4"* ]]; then
  #remove the SSL connector
  sed -i '/<Connector port="\${TOMCAT_PORT_SSL}" URIEncoding="UTF-8" protocol="org.apache.coyote.http11.Http11Protocol" SSLEnabled="true"/,+5d' $TOMCAT_SERVER_FILE
fi

if [ -n "$CATALINA_HOME" ]; then

  user="tomcat"
  if [[ $(stat -c %U /opt/alfresco/alf_data) != "$user" ]]; then
    chown -R $user:$user /opt/alfresco/alf_data
  fi
  if [[ $(stat -c %U "$CATALINA_HOME/temp") != "$user" ]]; then
    chown -R $user:$user "$CATALINA_HOME"/temp
  fi

fi

echo "Alfresco init done"
