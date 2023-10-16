#!/bin/bash

currentDate=$(date +%s)
if [ -z "${BUILD_DATE}" ]; then
  echo '{ "timestamp" : '"$(date -d @$currentDate +"%s")"' ,"severity" : "WARN", "type" : "application","component" : "docker-entrypoint" ,"fullMessage" : "Unable to determine image age: BUILD_DATE is not set"}'
else
  printf -v buildDate "%.0f\n" "$BUILD_DATE"
  timeelapsed=$(expr ${currentDate} - ${buildDate})
  if [ $timeelapsed -ge 2592000 ]; then # 30 days
    echo '{ "timestamp" : '"$(date -d @$currentDate +"%s")"' ,"severity" : "WARN", "type" : "application","component" : "docker-entrypoint" , "fullMessage" : "This is an old image: BUILD_DATE='"$(date -d @$BUILD_DATE +"%Y-%m-%d %H:%M:%S")"' and currentDate='"$(date -d @$currentDate +"%Y-%m-%d %H:%M:%S")"'"}'
  fi
fi
