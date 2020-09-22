#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -exv

echo "Alfresco init start"

export DB_HOST=${DB_HOST:-'postgresql'}
export DB_PORT=${DB_PORT:-'5432'}
export DB_NAME=${DB_NAME:-'alfresco'}
export SOLR_SSL=${SOLR_SSL:-'https'}
export JAVA_OPTS

ENABLE_CHOWNCUTOFF=${ENABLE_CHOWNCUTOFF:-'true'}
CHOWNCUTOFF=${CHOWN_CUTOFF:-10000}

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
  if [[ ($(stat -c %U /opt/alfresco/alf_data) != "$user" && $(stat -c %a /opt/alfresco/alf_data) -lt 766) ]]; then
    if [[ "$ENABLE_CHOWNCUTOFF" == 'true' ]]; then
        # check if number of files exceeds cutoff value to prevent longrunning chown process.
        find /opt/alfresco/alf_data | head -n $CHOWNCUTOFF > /dev/null
        # * pipestatus will be 0 if the above find ended before head ended.
        # * pipestatus will be 141 if the above head ends before find -which implies there are more than CHOWNCUTOFF files,
        #   which means the following chown would be expensive in time.
        #   sigpipe will cause the find to end.
        if [[ ${PIPESTATUS[0]} -eq 0 ]]; then
          chown -R $user:$user /opt/alfresco/alf_data
        else
          # custom exit code for debug to this script
          exit 64
        fi
    else
        chown -R $user:$user /opt/alfresco/alf_data
    fi
  fi
  if [[ ($(stat -c %U "$CATALINA_HOME/temp") != "$user" && $(stat -c %a "$CATALINA_HOME/temp") -lt 766) ]]; then
    if [[ "$ENABLE_CHOWNCUTOFF" == 'true' ]]; then
        find "$CATALINA_HOME/temp" | head -n $CHOWNCUTOFF > /dev/null
        if [[ ${PIPESTATUS[0]} -eq 0 ]]; then
          chown -R $user:$user "$CATALINA_HOME"/temp
        else
          exit 65
        fi
    else
        chown -R $user:$user "$CATALINA_HOME"/temp
    fi
  fi

fi

echo "Alfresco init done"
