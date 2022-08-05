#!/bin/bash

currentDate=`date +%s`
if [ -z "${BUILD_DATE}" ]
then
    1>&2 echo "$(tput -Txterm setaf 3)****************************************************$(tput -Txterm sgr0)"
    1>&2 echo "$(tput -Txterm setaf 3)Unable to determine image age: BUILD_DATE is not set$(tput -Txterm sgr0)"
    1>&2 echo "$(tput -Txterm setaf 3)****************************************************$(tput -Txterm sgr0)"
else
    timeelapsed=`expr ${currentDate} - ${BUILD_DATE}`
    if [ $timeelapsed -ge 2592000 ] # 30 days
    then
	1>&2 echo "$(tput -Txterm setaf 3)****************************************************************************************$(tput -Txterm sgr0)"
	1>&2 echo "$(tput -Txterm setaf 3)This is an old image: BUILD_DATE=$(date -d @$BUILD_DATE +"%Y-%m-%d %H:%M:%S") and currentDate=$(date -d @$currentDate +"%Y-%m-%d %H:%M:%S")$(tput -Txterm sgr0)"
	1>&2 echo "$(tput -Txterm setaf 3)****************************************************************************************$(tput -Txterm sgr0)"
    fi
fi
