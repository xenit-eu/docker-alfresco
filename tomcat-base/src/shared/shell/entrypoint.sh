#!/bin/sh
export EXTRA_JAVA_OPTS=""
for i in `env`
do
	if echo $i | grep "^JAVA_OPTS_.*" > /dev/null ;
	    then
	    value=`echo $i | cut -d '=' -f 2-`
	    export EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $value"
	fi
done

java $JAVA_OPTS $EXTRA_JAVA_OPTS -cp /app/tomcat-embedded/xenit-tomcat-embedded.jar:/app/tomcat-embedded/lib/* eu.xenit.alfresco.tomcat.embedded.Main