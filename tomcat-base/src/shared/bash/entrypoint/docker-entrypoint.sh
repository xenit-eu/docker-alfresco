#!/bin/bash

DIR=/docker-entrypoint.d

if [[ -d "$DIR" ]]
then
  for SCRIPT in ${DIR}/*; do
    echo "> $(tput -Txterm bold)Executing ${SCRIPT}$(tput -Txterm sgr0)"
    . "$SCRIPT"
  done
fi

echo "$(tput -Txterm bold)> Starting CMD:$(tput -Txterm sgr0) $@"
echo "$(tput -Txterm dim)> exec $(eval eval echo $@)$(tput -Txterm sgr0)"

# first 'eval' expands variables (like $JAVA_OPTS) in $@
# second 'eval' flattens arguments, so that $JAVA_OPTS expanded into is not seen as a single argument (DOCKER-136)
# which enables us to use exec-form with $JAVA_OPTS support:
# -> `CMD ["java", "$JAVA_OPTS", "-jar", "foo.jar"]`
eval eval exec $@
