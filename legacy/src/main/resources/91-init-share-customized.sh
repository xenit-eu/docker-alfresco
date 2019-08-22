#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -e

echo "Share init start"

ALFRESCO_HOST=${ALFRESCO_HOST:-alfresco}
ALFRESCO_PORT=${ALFRESCO_PORT:-8080}
ALFRESCO_PROTOCOL=${ALFRESCO_PROTOCOL:-http}
ALFRESCO_CONTEXT=${ALFRESCO_CONTEXT:-alfresco}

sed -e 's/http:\/\/localhost:8080\/alfresco/'"$ALFRESCO_PROTOCOL"':\/\/'"$ALFRESCO_HOST"':'"$ALFRESCO_PORT"'\/'"$ALFRESCO_CONTEXT"'/g' </docker-config/share-config-custom.xml >"${CATALINA_HOME}/shared/classes/alfresco/web-extension/share-config-custom.xml"


echo "Share init done"

