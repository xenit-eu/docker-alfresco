#!/bin/bash

currentDate=`date +%s`
timeelapsed=`expr ${currentDate} - ${BUILD_DATE}`
if [ $timeelapsed -ge 2592000 ] # 30 days
then
    echo "************"
    echo "This is an old image: BUILD_DATE=$(date -d @$BUILD_DATE +"%Y-%m-%d %H:%M:%S") and currentDate=$(date -d @$currentDate +"%Y-%m-%d %H:%M:%S")"
    echo "*************"
fi


