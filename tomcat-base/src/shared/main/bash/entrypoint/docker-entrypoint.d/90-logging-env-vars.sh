#!/bin/bash
set -e

# This script generates a complete log4j configuration file based on
# LOG_LEVEL_ environment variables and tells the Java process to use it.

function log() {
  echo '{ "timestamp" : '"$(date '+%s')"' ,"severity" : "INFO", "type" : "entrypoint","component" : "docker-entrypoint" ,"fullMessage" : "'"$1"'"}'
}

if [ "$LOG4J_VERSION" = "1" ]; then
  FNAME="log4j.properties"
else
  FNAME="log4j2.properties"
fi

LOG_BASE_FILE="/log4j.properties.base"
LOG_CONFIG_FILE="/usr/local/tomcat/webapps/alfresco/WEB-INF/classes/${FNAME}"

log "Generating log config at: ${LOG_CONFIG_FILE}"
log "Using Log4j Version: ${LOG4J_VERSION}"

# Start with the base file
cp "${LOG_BASE_FILE}" "${LOG_CONFIG_FILE}"

# Now generate logger configs from env variables
# We need to pre-process for log4j2's 'loggers = name1,name2' syntax
logger_list=""
logger_configs=""

for var in $(env); do
  if echo "$var" | grep -q "^LOG_LEVEL_"; then
    var_name=$(echo "$var" | cut -d= -f1)
    var_value=$(echo "$var" | cut -d= -f2)

    # Convert LOG_LEVEL_org_alfresco -> org.alfresco
    logger_name=$(echo "$var_name" | sed 's/LOG_LEVEL_//' | sed 's/_/./g')

    if [ -n "$logger_name" ] && [ -n "$var_value" ]; then
      log "Applying log level: ${logger_name}=${var_value}"

      if [ "$LOG4J_VERSION" = "1" ]; then
        # Log4j 1.x syntax
        logger_configs="${logger_configs}log4j.logger.${logger_name}=${var_value}\n"
      else
        # Log4j 2.x syntax
        logger_list="${logger_list}${logger_name},"
        # Add the logger definition
        logger_configs="${logger_configs}logger.${logger_name}.name = ${logger_name}\n"
        logger_configs="${logger_configs}logger.${logger_name}.level = ${var_value}\n"
      fi
    fi
  fi
done

# Add default appender and root configuration
# TODO: Specify a base configuration file somewhere in the project instead and use that

if [ "$LOG4J_VERSION" = "1" ]; then
  # --- Log4j 1.x Config ---

  # Add the custom loggers
  echo -e "\n# Custom Log Levels" >> "${LOG_CONFIG_FILE}"
  echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"

  # Tell Java to use this file
  JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=file://${LOG_CONFIG_FILE}"
else
  # --- Log4j 2.x Config ---

  # Add the custom loggers
  echo -e "\n# Custom Log Levels" >> "${LOG_CONFIG_FILE}"
  if [ -n "$logger_list" ]; then
    # Add the 'loggers = name1,name2' list (stripping last comma)
    echo "loggers = ${logger_list%,}" >> "${LOG_CONFIG_FILE}"
    # Add the logger definitions
    echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"
  fi

  # Tell Java to use this file
  JAVA_OPTS="$JAVA_OPTS -Dlog4j.configurationFile=${LOG_CONFIG_FILE}"
fi

# Export new Java options with updated log config
export JAVA_OPTS

log "Log configuration complete. JAVA_OPTS updated."