# Alfresco in Docker and Alfresco Share in Docker

> [![Xenit Logo](https://xenit.eu/wp-content/uploads/2017/09/XeniT_Website_Logo.png)](https://xenit.eu/open-source)
>
> docker-alfresco is a part of the Xenit Open Source Tooling around Alfresco. Xenit is company with a big expertise and
> a strong team around Alfresco. If you'd like to learn more about our
> [tools](https://xenit.eu/open-source), [services](https://xenit.eu/alfresco) and
> [products](https://xenit.eu/alfresco-products), please visit our [website](https://xenit.eu).

This project builds Alfresco-specific docker images used by Xenit, starting with Alfresco 6.1.

## Images created

* [`docker.io/xenit/tomcat`] = xenit specific tomcat images
* [`docker.io/xenit/alfresco-repository-skeleton`] = skeleton common for all Alfresco images per major version; includes java,
  tomcat, init script, keystore
* [`docker.io/xenit/alfresco-repository-community`] = community Alfresco images
* [`docker.xenit.eu/alfresco-enterprise/alfresco-repository-enterprise`] = enterprise Alfresco images
* [`docker.io/xenit/alfresco-share-skeleton`] = community Share images
* [`docker.io/xenit/alfresco-share-community`] = skeleton common for all Share images per major version; includes java,
  tomcat, init script

## Supported Tags

* [`:7.2.0`, `:7.2`, `:7`] = major, minor, revision

## Overview

This is Xenit's repository for Alfresco and Share docker images. A major-minor version has a common skeleton.

## Image Variants

### Repository-only:

* alfresco-repository-enterprise:<version>
* alfresco-repository-community:<version>

These are the images used for a multi-container Alfresco enterprise deployment in production. To be used together with
Share Docker Image , [`docker-solr`](https://github.com/xenit-eu/docker-solr).

The most basic setup uses the docker-compose files
from [`2repository/src/integrationTest/resources`](https://github.com/xenit-eu/docker-alfresco/tree/master/2repository/src/integrationTest/resources)
. These files are used in the integration tests.

### Share-only:

Multi-container: `share` to be used together with Alfresco Docker Image
, [`docker-solr`](https://github.com/xenit-eu/docker-solr), [`postgres`](https://github.com/xenit-eu/docker-postgres).

### Repository and share:

Images with the repository and share are no longer built.

## Environment variables

There are several environment variables available to tweak the behaviour. While none of the variables are required, they
may significantly aid you in using these images. We don't set default environment variables. The defaults that are documented
in the following tables, are the values that are used when the environment variables are not set.

### Common Environment variables

| Variable                                            | Default                          | Description                                                                                                                                                                                                                                                                                              |
|-----------------------------------------------------|----------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| TOMCAT_WEBAPPS                                      | /usr/local/tomcat/webapps        |                                                                                                                                                                                                                                                                                                          |
| TOMCAT_BASE_DIR                                     | /usr/local/tomcat/temp           |                                                                                                                                                                                                                                                                                                          |
| SHARED_LIB_DIR                                      | /usr/local/tomcat/shared/lib     |                                                                                                                                                                                                                                                                                                          |
| GENERATED_CLASSPATH_DIR                             | /dev/shm/classpath               |                                                                                                                                                                                                                                                                                                          |
| SHARED_CLASSPATH_DIR                                | /usr/local/tomcat/shared/classes |                                                                                                                                                                                                                                                                                                          |
| TOMCAT_PORT                                         | 8080                             | non SSL port tomcat is listening on                                                                                                                                                                                                                                                                      |
| TOMCAT_PORT_SSL                                     | 8443                             | SSL port tomcat is listening on                                                                                                                                                                                                                                                                          |
| TOMCAT_SERVER_PORT                                  | 8005                             | Port for server communication                                                                                                                                                                                                                                                                            |
| TOMCAT_MAX_HTTP_HEADER_SIZE                         | 32768                            | Maximum http header size                                                                                                                                                                                                                                                                                 |
| TOMCAT_MAX_THREADS                                  | 200                              | Maximum number of threads                                                                                                                                                                                                                                                                                |
| TOMCAT_ALLOW_CASUAL_MULTIPART_PARSING               | false                            | Set to true if Tomcat should automatically parse multipart/form-data request bodies when HttpServletRequest.getPart* or HttpServletRequest.getParameter* is called. The default is false.                                                                                                                |
| TOMCAT_ALLOW_MULTIPLE_LEADING_FORWARD_SLASH_IN_PATH | false                            | Tomcat will collapse multiple leading / characters at the start of the return value for HttpServletRequest#getContextPath() to a single /. The default is false.                                                                                                                                         |
| TOMCAT_CROSS_CONTEXT                                | false                            | Set to true if you want calls within this application to ServletContext.getContext() to successfully return a request dispatcher for other web applications running on this virtual host. Set to false (the default) in security conscious environments, to make getContext() always return null.        |
| TOMCAT_REMOTE_IP_VALVE_ENABLED                      | true                             | If this is set to true, it will allow Tomcat to pick up remote ip headers like "X-Forwarded-For“, “X-Forwarded-Proto“ and “X-Forwarded-Port“. If Alfresco/Share are behind a proxy, they will be aware of that.                                                                                          |
| JAVA_XMS                                            |                                  | -Xmx                                                                                                                                                                                                                                                                                                     |
| JAVA_XMX                                            |                                  | -Xms                                                                                                                                                                                                                                                                                                     |
| DEBUG                                               | false                            | -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n                                                                                                                                                                                                                                    |
| JMX_ENABLED                                         | false                            | -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote.port=5000 -Djava.rmi.server.hostname=$JMX_RMI_HOST |
| JMX_RMI_HOST                                        | 0.0.0.0                          |                                                                                                                                                                                                                                                                                                          |
| JAVA_OPTS_\<variable\>=\<value\>                    | \<value\>                        | \<variable\>                                                                                                                                                                                                                                                                                             |
| LOG_LEVEL_\<org_package_name\>=\<value\>            |                                  | Sets the log level for org.package.name to the provided value                                                                                                                                                                                                                                            |
| JSON_LOGGING                                        | false                            | When true, all logs will be in JSON.                                                                                                                                                                                                                                                                     |
| ACCESS_LOGGING                                      | false                            | When true, access logs will be printed. These logs are always in JSON format                                                                                                                                                                                                                             |
| EXIT_ON_FAILURE                                     | true                             | When true, the Java process will exit when the application deployed in Tomcat fails to start. Can be turned of for debugging purposes.                                                                                                                                                                   |
| TOMCAT_CACHE_MAX_SIZE                               | 100000                           |                                                                                                                                                                                                                                                                                                          |

### Alfresco Environment variables

The alfresco-global.properties can be set via a generic mechanism by setting environment variables of the form
GLOBAL_<parameter>, e.g. GLOBAL_alfresco.host.

A subset of the alfresco-global.properties have also dedicated environment variables e.g. SOLR_SSL. Generic variables
take precedence.

Environment variables:

| Variable                                     | alfresco-global.property variable              | Default                                     | Description                                                                                         |
|----------------------------------------------|------------------------------------------------|---------------------------------------------|-----------------------------------------------------------------------------------------------------|
| ALFRESCO_HOST                                | alfresco.host                                  | alfresco                                    |                                                                                                     |
| ALFRESCO_PORT                                | alfresco.port                                  | 8080                                        |                                                                                                     |
| ALFRESCO_PROTOCOL                            | alfresco.protocol                              | http                                        |                                                                                                     |
| SHARE_HOST                                   | share.host                                     | share                                       |                                                                                                     |
| SHARE_PORT                                   | share.port                                     | 8080                                        |                                                                                                     |
| SHARE_PROTOCOL                               | share.protocol                                 | http                                        |                                                                                                     |
| DB_DRIVER                                    | db.driver                                      | org.postgresql.Driver                       |                                                                                                     |
| DB_HOST                                      | db.host                                        | localhost                                   |                                                                                                     |
| DB_PORT                                      | db.port                                        | 5432                                        |                                                                                                     |
| DB_NAME                                      | db.name                                        | alfresco                                    |                                                                                                     |
| DB_USERNAME                                  | db.username                                    | alfresco                                    |                                                                                                     |
| DB_PASSWORD                                  | db.password                                    | admin                                       |                                                                                                     |
| DB_URL                                       | db.url                                         | jdbc:postgresql://postgresql:5432/alfresco  |                                                                                                     |
| DB_QUERY                                     | db.pool.validate.query                         | select 1                                    |                                                                                                     |
| INDEX                                        | index.subsystem.name                           | solr6                                       |                                                                                                     |
| SOLR_HOST                                    | solr.host                                      | solr                                        |                                                                                                     |
| SOLR_PORT                                    | solr.port                                      | 8080                                        |                                                                                                     |
| SOLR_PORT_SSL                                | solr.port.ssl                                  | 8443                                        |                                                                                                     |
| SOLR_SSL                                     | solr.secureComms                               | https                                       | When using value `secret`, the global property environment variable `GLOBAL_solr.sharedSecret=<secret-value>` should be added to this container. The solr should also be configured with the appropriate properties and secret value. |
| ENABLE_CLUSTERING                            | alfresco.cluster.enabled                       | false                                       |                                                                                                     |
| TOMCAT_SSL_KEYSTORE                          | encryption.ssl.keystore.location               | /keystore/ssl.keystore                      | Path for the ssl keystore file. Added to both the Tomcat connector and alfresco-global.properties   |
| TOMCAT_SSL_KEYSTORE_KEY_META_DATA_LOCATION   | encryption.ssl.keystore.keyMetaData.location   | /keystore/keystore-passwords.properties     | Path for the ssl keystore file. Added to both the Tomcat connector and alfresco-global.properties   |
| TOMCAT_SSL_KEYSTORE_PASSWORD                 | ssl-keystore.password                          |                                             | Password for the ssl keystore. Added to both the Tomcat connector and alfresco-global.properties    |
| TOMCAT_SSL_TRUSTSTORE                        | encryption.ssl.truststore.location             | /keystore/ssl.truststore                    | Path for the ssl truststore file. Added to both the Tomcat connector and alfresco-global.properties |
| TOMCAT_SSL_TRUSTSTORE_KEY_META_DATA_LOCATION | encryption.ssl.truststore.keyMetaData.location | /keystore/ssl-keystore-passwords.properties | Path for the ssl truststore file. Added to both the Tomcat connector and alfresco-global.properties |
| TOMCAT_SSL_TRUSTSTORE_PASSWORD               | ssl-truststore.password                        |                                             | Password for the ssl truststore. Added to both the Tomcat connector and alfresco-global.properties  |
| DIR_ROOT                                     | dir.root                                       | /opt/alfresco/alf_data                      |                                                                                                     |
| GLOBAL_\<variable\>                          | \<variable\>                                   |                                             |                                                                                                     |

### Share Environment variables

There are several environment variables available to tweak the behaviour. While none of the variables are required, they
may significantly aid you in using these images. The variables are read by an init script which further replaces them in
the relevant files. Such relevant files include:

* share-config-custom.xml

See also environment variables from lower layers: [`docker-openjdk`](https://github.com/xenit-eu/docker-openjdk)
and [`docker-tomcat`](https://github.com/xenit-eu/docker-tomcat).

Environment variables:

| Variable                   | Default                                | Description                                                                                                                                                    |
|----------------------------|----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ALFRESCO_HOST              | alfresco                               | Used to generate WebDav links.                                                                                                                                 |
| ALFRESCO_PORT              | 8080                                   | Used to generate WebDav links.                                                                                                                                 |
| ALFRESCO_PROTOCOL          | http                                   | Used to generate WebDav links.                                                                                                                                 |
| ALFRESCO_CONTEXT           | alfresco                               | Used to generate WebDav links.                                                                                                                                 |
| ALFRESCO_INTERNAL_HOST     | alfresco                               | Used for communication share-alfresco.                                                                                                                         |
| ALFRESCO_INTERNAL_PORT     | 8080                                   | Used for communication share-alfresco.                                                                                                                         |
| ALFRESCO_INTERNAL_PROTOCOL | http                                   | Used for communication share-alfresco.                                                                                                                         |
| ALFRESCO_INTERNAL_CONTEXT  | alfresco                               | Used for communication share-alfresco.                                                                                                                         |
| SHARE_CONFIG_PATH          | alfresco/web-extension                 | Parent folder for the share-config-custom.xml relative to GENERATED_CLASSPATH_DIR	                                                                             |
| SHARE_CONFIG_TEMPLATE_FILE | /docker-config/share-config-custom.xml | Path for a template file for share-config-custom.xml. This file supports variable replacement of the above variables, but also any other environment variable. |

If environment variables are not sufficient to cover the use-case desired, a custom share-config-custom.xml file can be
mounted in /docker-config/share-config-custom.xml.

## Support & Collaboration

These images are updated via pull requests to
the [xenit-eu/docker-alfresco/](https://github.com/xenit-eu/docker-alfresco/) Github-repository.

### How to build

To build a local version of the _alfresco_ images:

```
./gradlew buildDockerImage
```

or for a specific image:

```
./gradlew 2repository:enterprise-6.1.0:buildDockerImage
```

To run the integration tests:

```
./gradlew integrationTests
```

To see all available tasks:

```
./gradlew tasks
```

If you have access to [Alfresco private repository](https://artifacts.alfresco.com/nexus/content/groups/private/) add
the repository to build.gradle and add

```
-Penterprise
```

to your build commands.
