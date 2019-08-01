#!/bin/bash

# we should get env ${CATALINA_HOME} from upstream container docker
set -e

echo "Alfresco init start"

JAVA_XMS=${JAVA_XMS:-'2048M'}
JAVA_XMX=${JAVA_XMX:-'2048M'}
DEBUG=${DEBUG:-'false'}
JMX_ENABLED=${JMX_ENABLED:-'false'}
JMX_RMI_HOST=${JMX_RMI_HOST:-'0.0.0.0'}
DB_HOST=${DB_HOST:-'postgresql'}
DB_PORT=${DB_PORT:-'5432'}
DB_NAME=${DB_NAME:-'alfresco'}

CONFIG_FILE=${CONFIG_FILE:-${CATALINA_HOME}'/shared/classes/alfresco-global.properties'}
TOMCAT_CONFIG_FILE=${CATALINA_HOME}'/bin/setenv.sh'
TOMCAT_SERVER_FILE=${CATALINA_HOME}'/conf/server.xml'

SOLR_SSL=${SOLR_SSL:-'https'}


# sets an Alfresco parameter in alfresco-global.properties-not-used
function setGlobalOption {
if [ -n "$CONFIG_FILE" ]
then
    key="$1"
    value="$2"
    if grep --quiet -e "^$key=" "$CONFIG_FILE"
    then
        echo "Key already exists $key=$value, removing it"
        # remove the old line
        sed -i -E "s|(^$key=(.*?)$)||" $CONFIG_FILE
    fi
        # add a new line with the option
        echo "Adding $key=$value"
        echo "$key=$value" >> $CONFIG_FILE
fi
}


# the key is ignored, the value should contain the "-D" flag if it's a property
function setJavaOption {
    JAVA_OPTS="$JAVA_OPTS $2"
}

function setGlobalOptions {
    IFS=$'\n'
    for i in `env`
    do
	if [[ $i == GLOBAL_* ]]
	    then
	    key=`echo $i | cut -d '=' -f 1 | cut -d '_' -f 2-`
	    value=`echo $i | cut -d '=' -f 2-`
	    setGlobalOption $key $value
	fi
    done
}

function setJavaOptions {
    IFS=$'\n'
    for i in `env`
    do
	if [[ $i == JAVA_OPTS_* ]]
	    then
	    key=`echo $i | cut -d '=' -f 1 | cut -d '_' -f 3-`
	    value=`echo $i | cut -d '=' -f 2-`
	    setJavaOption $key $value
	fi
    done
}

setGlobalOption 'dir.root' "${DIR_ROOT:-/opt/alfresco/alf_data}"
setGlobalOption 'dir.keystore' "${DIR_KEYSTORE:-/opt/alfresco/keystore}"

setGlobalOption 'alfresco.host' "${ALFRESCO_HOST:-alfresco}"
setGlobalOption 'alfresco.port' "${ALFRESCO_PORT:-8080}"
setGlobalOption 'alfresco.protocol' "${ALFRESCO_PROTOCOL:-http}"
setGlobalOption 'alfresco.context' "${ALFRESCO_CONTEXT:-alfresco}"

setGlobalOption 'share.host' "${SHARE_HOST:-share}"
setGlobalOption 'share.port' "${SHARE_PORT:-8080}"
setGlobalOption 'share.protocol' "${SHARE_PROTOCOL:-http}"
setGlobalOption 'share.context' "${SHARE_CONTEXT:-share}"

setGlobalOption 'db.driver' "${DB_DRIVER:-org.postgresql.Driver}"
setGlobalOption 'db.host' "${DB_HOST}"
setGlobalOption 'db.port' "${DB_PORT}"
setGlobalOption 'db.name' "${DB_NAME}"
setGlobalOption 'db.username' "${DB_USERNAME:-alfresco}"
setGlobalOption 'db.password' "${DB_PASSWORD:-admin}"
setGlobalOption 'db.url' "${DB_URL:-jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME}"
setGlobalOption 'db.pool.validate.query' "${DB_QUERY:-select 1}"

# Search
if [[ $ALFRESCO_VERSION = "4"* ]]  
then
    setGlobalOption 'index.subsystem.name' "${INDEX:-solr}"
    if [[ -z $INDEX ]] ; then INDEX=solr ; fi
elif [[ $ALFRESCO_VERSION = "5.0"* ]] || [[ $ALFRESCO_VERSION = "5.1"* ]]
then
    setGlobalOption 'index.subsystem.name' "${INDEX:-solr4}"
    if [[ -z $INDEX ]] ; then INDEX=solr4 ; fi
elif [[ $ALFRESCO_VERSION = "5.2"* ]]
then
    setGlobalOption 'index.subsystem.name' "${INDEX:-solr6}"
    if [[ -z $INDEX ]] ; then INDEX=solr6 ; fi
else
    setGlobalOption 'index.subsystem.name' "${INDEX:-solr6}"
    if [[ -z $INDEX ]] ; then INDEX=solr6 ; fi
fi
# if solr6, change backup locations for Solr
if [[ -n $INDEX && $INDEX = "solr6" ]]
then
    SOLR_INSTALL_HOME='/opt/alfresco-search-services'
    setGlobalOption 'solr.backup.alfresco.remoteBackupLocation' "${SOLR_INSTALL_HOME}/data/solr6Backup/alfresco"
    setGlobalOption 'solr.backup.archive.remoteBackupLocation' "${SOLR_INSTALL_HOME}/data/solr6Backup/archive"        
fi


setGlobalOption 'solr.host' "${SOLR_HOST:-solr}"
setGlobalOption 'solr.port' "${SOLR_PORT:-8080}"
setGlobalOption 'solr.port.ssl' "${SOLR_PORT_SSL:-8443}"
setGlobalOption 'solr.useDynamicShardRegistration' "${DYNAMIC_SHARD_REGISTRATION:-false}"
setGlobalOption 'solr.secureComms' "${SOLR_SSL:-https}"
if [[ $SOLR_SSL = none ]] && [[ $ALFRESCO_VERSION != "5.0"* ]] && [[ $ALFRESCO_VERSION != "3"* ]] && [[ $ALFRESCO_VERSION != "4"* ]]
then
#remove the SSL connector
sed -i '/<Connector port="\${TOMCAT_PORT_SSL}" URIEncoding="UTF-8" protocol="org.apache.coyote.http11.Http11Protocol" SSLEnabled="true"/,+5d' $TOMCAT_SERVER_FILE
fi

# System
setGlobalOption 'mail.host' "${MAIL_HOST:-localhost}"
setGlobalOption 'cifs.enabled' "${ENABLE_CIFS:-false}" # CIFS Configuration
setGlobalOption 'ftp.enabled' "${ENABLE_FTP:-false}" # FTP Configuration
setGlobalOption 'replication.enabled' "${ENABLE_REPLICATION:-false}" # Replication Configuration
setGlobalOption 'alfresco.cluster.enabled' "${ENABLE_CLUSTERING:-false}" # Cluster Configuration

# community versions works with ooo and Alfresco recommends to have only one system enabled at a time
# https://docs.alfresco.com/4.2/concepts/OOo-subsystems-intro.html
if [[ $ALFRESCO_FLAVOR = "community" ]]
then
setGlobalOption 'ooo.enabled' 'true'
setGlobalOption 'jodconverter.enabled' 'false'
else
setGlobalOption 'ooo.enabled' 'false'
setGlobalOption 'jodconverter.enabled' 'true'
fi

setGlobalOption 'ooo.exe' '/usr/lib/libreoffice/program/soffice'
setGlobalOption 'jodconverter.officeHome' '/usr/lib/libreoffice/'
setGlobalOption 'img.exe' "/usr/bin/convert"
setGlobalOption 'img.root' "/etc/ImageMagick"
setGlobalOption 'img.dyn' "/usr/lib"
setGlobalOption 'swf.exe' '/usr/bin/pdf2swf'
setGlobalOption 'alfresco-pdf-renderer.root' "/opt/alfresco"
setGlobalOption 'alfresco-pdf-renderer.exe' '${alfresco-pdf-renderer.root}/alfresco-pdf-renderer'


if [ $JMX_ENABLED = true ]
then
    JAVA_OPTS="$JAVA_OPTS -Xms$JAVA_XMS -Xmx$JAVA_XMX -Dfile.encoding=UTF-8 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote.port=5000 -Djava.rmi.server.hostname=$JMX_RMI_HOST"
    setGlobalOption 'alfresco.jmx.connector.enabled' 'true'
else
    JAVA_OPTS="$JAVA_OPTS -Xms$JAVA_XMS -Xmx$JAVA_XMX -Dfile.encoding=UTF-8"
fi

if [ $DEBUG = true ]
then
    JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=0.0.0.0:8000,server=y,suspend=n"
fi

setGlobalOptions
setJavaOptions

# special handling for Alfresco 4.2 - set MaxPermSize
if [[ $ALFRESCO_VERSION = "4"* ]]
then   
    setJavaOption MaxPerm -XX:MaxPermSize=256m
fi

# special handling for Alfresco 6.1 - set messaging.subsystem.autoStart=false
if [[ $ALFRESCO_VERSION = "6.1"* ]]
then   
    setGlobalOption 'messaging.subsystem.autoStart' 'false'
fi

if [ -n "$CATALINA_HOME" ]
then

echo "JAVA_OPTS=\"$JAVA_OPTS\"" >$TOMCAT_CONFIG_FILE
echo "export JAVA_OPTS" >> $TOMCAT_CONFIG_FILE

user="tomcat"
if [[ $(stat -c %U /opt/alfresco/alf_data) != "$user" ]]
  then
      chown -R $user:$user /opt/alfresco/alf_data
fi
if [[ $(stat -c %U "$CATALINA_HOME/temp") != "$user" ]]
  then
      chown -R $user:$user "$CATALINA_HOME"/temp
fi

fi

echo "Alfresco init done"
