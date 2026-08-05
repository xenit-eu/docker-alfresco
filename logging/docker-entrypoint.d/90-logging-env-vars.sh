#!/bin/bash
set -e

# This script generates a complete log4j configuration file based on
# LOG_LEVEL_ environment variables and installs it into the WEB-INF/classes
# of every initialised webapp, so each webapp picks it up from its own classpath.

log() {
  echo '{ "timestamp" : '"$(date '+%s')"' ,"severity" : "INFO", "type" : "entrypoint","component" : "docker-entrypoint" ,"fullMessage" : "'"$1"'"}'
}

LOG_BASE_FILE="/log4j.properties.base"
WEBAPPS_DIR="/usr/local/tomcat/webapps"

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
  log "Using Log4j Version: ${LOG4J_VERSION}"

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

  # Install the generated config into every initialised webapp.
  for webapp_dir in "${WEBAPPS_DIR}"/*/; do
    [ -d "$webapp_dir" ] || continue

    webapp_name="$(basename "$webapp_dir")"
    LOG_CONFIG_FILE="${webapp_dir}WEB-INF/classes/${FNAME}"

    mkdir -p "${webapp_dir}WEB-INF/classes"

    log "Generating log config for webapp '${webapp_name}' at: ${LOG_CONFIG_FILE}"

    # Start from the base file, then append the generated logger section.
    cp "${LOG_BASE_FILE}" "${LOG_CONFIG_FILE}"
    echo -e "\n# Custom Log Levels" >> "${LOG_CONFIG_FILE}"

    if [ "$LOG4J_VERSION" = "1" ]; then
      echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"
    elif [ -n "$logger_list" ]; then
      # Add the 'loggers = name1,name2' list (stripping last comma)
      echo "loggers = ${logger_list%,}" >> "${LOG_CONFIG_FILE}"
      echo -e "${logger_configs}" >> "${LOG_CONFIG_FILE}"
    fi
  done

  log "Log configuration complete."
fi