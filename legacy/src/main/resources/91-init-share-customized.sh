#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -e

if [ -d "$CATALINA_HOME/webapps/share" ]; then
echo "Share init start"

ALFRESCO_HOST=${ALFRESCO_HOST:-alfresco}
ALFRESCO_PORT=${ALFRESCO_PORT:-8080}
ALFRESCO_PROTOCOL=${ALFRESCO_PROTOCOL:-http}
ALFRESCO_CONTEXT=${ALFRESCO_CONTEXT:-alfresco}

export ALFRESCO_HOST
export ALFRESCO_PORT
export ALFRESCO_PROTOCOL
export ALFRESCO_CONTEXT

envsubst </docker-config/share-config-custom.xml >"${CATALINA_HOME}/shared/classes/alfresco/web-extension/share-config-custom.xml"

echo "Share init done"
fi
