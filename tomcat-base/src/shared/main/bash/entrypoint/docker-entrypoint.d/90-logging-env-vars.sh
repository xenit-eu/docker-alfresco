#!/bin/bash
set -e

# This script generates a complete log4j configuration file based on
# LOG_LEVEL_ environment variables and tells the Java process to use it.

log() {
  echo '{ "timestamp" : '"$(date '+%s')"' ,"severity" : "INFO", "type" : "entrypoint","component" : "docker-entrypoint" ,"fullMessage" : "'"$1"'"}'
}

LOG_BASE_FILE="/log4j.properties.base"

# Decide the output filename from the major log4j version.
case "$LOG4J_VERSION" in
  1) FNAME="log4j.properties" ;;
  2) FNAME="log4j2.properties" ;;
  *) FNAME="" ;;
esac

if [ -z "$FNAME" ]; then
  log "Major log4j version is unknown, skipping log config generation"
elif [ ! -f "$LOG_BASE_FILE" ]; then
  log "log4j.properties.base not found, skipping log config generation"
else
  LOG_CONFIG_FILE="/usr/local/tomcat/webapps/alfresco/WEB-INF/classes/${FNAME}"

  mkdir -p /usr/local/tomcat/webapps/alfresco/WEB-INF/classes

  log "Generating log config at: ${LOG_CONFIG_FILE}"
  log "Using Log4j Version: ${LOG4J_VERSION}"

  # Start from the base file.
  cp "${LOG_BASE_FILE}" "${LOG_CONFIG_FILE}"

  # Build logger configs from LOG_LEVEL_* env variables.
  # For log4j2 we also need a 'loggers = name1,name2' list.
  logger_list=""
  logger_configs=""

  while IFS='=' read -r var_name var_value; do
    # Skip anything that isn't a LOG_LEVEL_ variable.
    case "$var_name" in
      LOG_LEVEL_*) ;;
      *) continue ;;
    esac

    # Convert LOG_LEVEL_org_alfresco -> org.alfresco
    logger_name=$(echo "${var_name#LOG_LEVEL_}" | sed 's/_/./g')

    if [ -n "$logger_name" ] && [ -n "$var_value" ]; then
      log "Applying log level: ${logger_name}=${var_value}"

      if [ "$LOG4J_VERSION" = "1" ]; then
        # Log4j 1.x syntax
        logger_configs="${logger_configs}log4j.logger.${logger_name}=${var_value}\n"
      else
        # Log4j 2.x syntax
        logger_list="${logger_list}${logger_name},"
        logger_configs="${logger_configs}logger.${logger_name}.name = ${logger_name}\n"
        logger_configs="${logger_configs}logger.${logger_name}.level = ${var_value}\n"
      fi
    fi
  done < <(env)

  # Append the generated logger section and point Java at the file.
  echo -e "\n# Custom Log Levels" >> "${LOG_CONFIG_FILE}"

  if [ "$LOG4J_VERSION" = "1" ]; then
    echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"
    JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=file://${LOG_CONFIG_FILE}"
  else
    if [ -n "$logger_list" ]; then
      # Add the 'loggers = name1,name2' list (stripping last comma)
      echo "loggers = ${logger_list%,}" >> "${LOG_CONFIG_FILE}"
      echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"
    fi
    JAVA_OPTS="$JAVA_OPTS -Dlog4j.configurationFile=${LOG_CONFIG_FILE}"
  fi

  export JAVA_OPTS
  log "Log configuration complete. JAVA_OPTS updated."
fi