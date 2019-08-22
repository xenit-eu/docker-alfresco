#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -e

if [ -d "$CATALINA_HOME/webapps/share" ]; then
echo "Share init start"

JAVA_XMS=${JAVA_XMS:-'512M'}
JAVA_XMX=${JAVA_XMX:-'2048M'}
DEBUG=${DEBUG:-'false'}
JMX_ENABLED=${JMX_ENABLED:-'false'}
JMX_RMI_HOST=${JMX_RMI_HOST:-'0.0.0.0'}


ALFRESCO_HOST=${ALFRESCO_HOST:-alfresco}
ALFRESCO_PORT=${ALFRESCO_PORT:-8080}
ALFRESCO_PROTOCOL=${ALFRESCO_PROTOCOL:-http}
ALFRESCO_CONTEXT=${ALFRESCO_CONTEXT:-alfresco}

sed -e 's/http:\/\/localhost:8080\/alfresco/'"$ALFRESCO_PROTOCOL"':\/\/'"$ALFRESCO_HOST"':'"$ALFRESCO_PORT"'\/'"$ALFRESCO_CONTEXT"'/g' </docker-config/share-config-custom.xml >"${CATALINA_HOME}/shared/classes/alfresco/web-extension/share-config-custom.xml"

setJavaOption "defaults" "-Xms$JAVA_XMS -Xmx$JAVA_XMX -Dfile.encoding=UTF-8"

if [ $DEBUG = true ]
then
    setJavaOption "debug" "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000"
fi

if [ $JMX_ENABLED = true ]
then
    # be sure port 5000 is mapped on the host also on 5000
    setJavaOption "jmx" "-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote.port=5000 -Djava.rmi.server.hostname=${JMX_RMI_HOST}"
fi

echo "Share init done"
fi
