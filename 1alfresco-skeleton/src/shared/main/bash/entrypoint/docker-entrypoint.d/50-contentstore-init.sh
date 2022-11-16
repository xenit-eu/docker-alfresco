mkdir -p /opt/alfresco/alf_data
user="tomcat"
# Check existence of alf_data/contentstore
# -> if dir exists, check if the permissions are incorrect
# --> if permissions are incorrect, exit with error code
# --> else if correct, do nothing
# -> else if not exists, chown only alf_data
if [[ -e /opt/alfresco/alf_data/contentstore && -e /opt/alfresco/alf_data/contentstore.deleted ]]; then
  if [[ ($(stat -c %U /opt/alfresco/alf_data/contentstore) != "$user" && $(stat -c %a /opt/alfresco/alf_data/contentstore) -lt 766)\
   || ($(stat -c %U /opt/alfresco/alf_data/contentstore.deleted) != "$user" && $(stat -c %a /opt/alfresco/alf_data/contentstore.deleted) -lt 766) ]]; then
    # custom exit code for debug to this script
    echo '{ "timestamp" : '"$(date '+%s')"' ,"severity" : "ERROR", "type" : "application","component" : "docker-entrypoint" ,"fullMessage" : "Contentstores exists within /opt/alfresco/alf_data , but do not have correct ownership/permission to run alfresco. Exiting with code 64."}'
    exit 64
  fi
else
  echo '{ "timestamp" : '"$(date '+%s')"' ,"severity" : "INFO", "type" : "application","component" : "docker-entrypoint" ,"fullMessage" : "No contentstore in alf_data ; assuming dev/test environment, chowning alf_data to tomcat user."}'
  chown $user:$user /opt/alfresco/alf_data
fi