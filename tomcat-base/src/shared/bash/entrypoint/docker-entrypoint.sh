#!/bin/bash

DIR=/docker-entrypoint.d

if [[ -d "$DIR" ]]
then
  for SCRIPT in ${DIR}/*; do
    echo "> Executing ${SCRIPT}"
    . "$SCRIPT"
  done
fi

echo "> Starting CMD: $@"
echo "> exec $(eval eval echo $@)"

# first 'eval' expands variables (like $JAVA_OPTS) in $@
# second 'eval' flattens arguments, so that $JAVA_OPTS expanded into is not seen as a single argument (DOCKER-136)
# which enables us to use exec-form with $JAVA_OPTS support:
# -> `CMD ["java", "$JAVA_OPTS", "-jar", "foo.jar"]`
eval eval exec setpriv --reuid="tomcat" --regid="tomcat" --init-groups $@
