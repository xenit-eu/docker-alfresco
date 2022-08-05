#!/bin/bash

if [[ $JMX_ENABLED == true ]]
then
 JMX_RMI_HOST=${JMX_RMI_HOST:-'0.0.0.0'}
 JAVA_OPTS_JMX="-Dcom.sun.management.jmxremote.authenticate=false \
                -Dcom.sun.management.jmxremote.local.only=false \
                -Dcom.sun.management.jmxremote.ssl=false \
                -Dcom.sun.management.jmxremote \
                -Dcom.sun.management.jmxremote.rmi.port=5000 \
                -Dcom.sun.management.jmxremote.port=5000 \
                -Djava.rmi.server.hostname=${JMX_RMI_HOST}"
 export JAVA_OPTS_JMX
fi

if [[ $DEBUG == true ]]
then
 JAVA_OPTS_DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000"
 export JAVA_OPTS_DEBUG
fi

if [[ -n $JAVA_XMX ]]
then
 JAVA_OPTS_XMX="-Xmx${JAVA_XMX}"
 export JAVA_OPTS_XMX
fi

if [[ -n $JAVA_XMS ]]
then
 JAVA_OPTS_XMS="-Xms${JAVA_XMS}"
 export JAVA_OPTS_XMS
fi

IFS=$'\n'
for I in $(env); do
    if [[ $I == JAVA_OPTS_* ]]; then
        echo "-- collecting $I"
        VALUE=$(echo $I | cut -d '=' -f 2-)
        JAVA_OPTS="$JAVA_OPTS $VALUE"
    fi
done

# Trim leading/trailing whitespace
export JAVA_OPTS=$(echo $JAVA_OPTS | xargs echo)

echo "Result: JAVA_OPTS=${JAVA_OPTS}"