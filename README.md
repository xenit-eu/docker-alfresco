# Alfresco in Docker

> [![Xenit Logo](https://xenit.eu/wp-content/uploads/2017/09/XeniT_Website_Logo.png)](https://xenit.eu/open-source)
>
> docker-alfresco is a part of the Xenit Open Source Tooling around Alfresco. Xenit is company with a big expertise and
> a strong team around Alfresco. If you'd like to learn more about our 
> [tools](https://xenit.eu/open-source), [services](https://xenit.eu/alfresco) and 
> [products](https://xenit.eu/alfresco-products), please visit our [website](https://xenit.eu).

This project builds Alfresco-specific docker images used by Xenit, starting with Alfresco 4.2.

## Images created 

* [`alfresco-skeleton`] = skeleton common for all images per major version; includes java, tomcat, init script, keystore
* [`alfresco-repository-enterprise`] = enterprise Alfresco images
* [`alfresco-repository-community`] = community Alfresco images

## Supported Tags

* [`:7.2.0`, `:7.2`, `:7`] = major, minor, revision

## Overview

This is our repository with _Dockerfile_ s that are the canonical source for our Alfresco Docker images. 

All images are automatically built by [jenkins-2](https://jenkins-2.xenit.eu) and published to [hub.xenit.eu](https://hub.xenit.eu).
Community images are built by [Travis](https://travis-ci.org/xenit-eu/) and published to [docker hub](https://hub.docker.com/u/xenit).

## Image Variants

### Repository-only:
* alfresco-repository-enterprise:<version>
* alfresco-repository-community:<version>

These are the images used for a multi-container Alfresco enterprise deployment in production. To be used together with [`docker-share`](https://github.com/xenit-eu/docker-share), [`docker-solr`](https://github.com/xenit-eu/docker-solr), [`postgres`](https://github.com/xenit-eu/docker-postgres).

The most basic setup uses the docker-compose files from [`2repository/src/integrationTest/resources`](https://github.com/xenit-eu/docker-alfresco/tree/master/2repository/src/integrationTest/resources). These files are used in the integration tests.

### Repository and share:
Images with the repository and share are no longer built.


## Environment variables

There are several environment variables available to tweak the behaviour. While none of the variables are required, they may significantly aid you in using these images.

The alfresco-global.properties can be set via a generic mechanism by setting environment variables of the form GLOBAL_<parameter>, e.g. GLOBAL_alfresco.host. 

A subset of the alfresco-global.properties have also dedicated environment variables e.g. SOLR_SSL. Generic variables take precedence.

Environment variables:

| Variable                         | alfresco-global.property variable | Default                                    | Description                                                                                                                                                                                                                                                                                              |
|----------------------------------|-----------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ALFRESCO_HOST                    | alfresco.host                     | alfresco                                   |                                                                                                                                                                                                                                                                                                          |
| ALFRESCO_PORT                    | alfresco.port                     | 8080                                       |                                                                                                                                                                                                                                                                                                          |
| ALFRESCO_PROTOCOL                | alfresco.protocol                 | http                                       |                                                                                                                                                                                                                                                                                                          |
| SHARE_HOST                       | share.host                        | share                                      |                                                                                                                                                                                                                                                                                                          |
| SHARE_PORT                       | share.port                        | 8080                                       |                                                                                                                                                                                                                                                                                                          |
| SHARE_PROTOCOL                   | share.protocol                    | http                                       |                                                                                                                                                                                                                                                                                                          |
| DB_DRIVER                        | db.driver                         | org.postgresql.Driver                      |                                                                                                                                                                                                                                                                                                          |
| DB_HOST                          | db.host                           | localhost                                  |                                                                                                                                                                                                                                                                                                          |
| DB_PORT                          | db.port                           | 5432                                       |                                                                                                                                                                                                                                                                                                          |
| DB_NAME                          | db.name                           | alfresco                                   |                                                                                                                                                                                                                                                                                                          |
| DB_USERNAME                      | db.username                       | alfresco                                   |                                                                                                                                                                                                                                                                                                          |
| DB_PASSWORD                      | db.password                       | admin                                      |                                                                                                                                                                                                                                                                                                          |
| DB_URL                           | db.url                            | jdbc:postgresql://postgresql:5432/alfresco |                                                                                                                                                                                                                                                                                                          |
| DB_QUERY                         | db.pool.validate.query            | select 1                                   |                                                                                                                                                                                                                                                                                                          |
| SOLR_HOST                        | solr.host                         | solr                                       |                                                                                                                                                                                                                                                                                                          |
| SOLR_PORT                        | solr.port                         | 8080                                       |                                                                                                                                                                                                                                                                                                          |
| SOLR_PORT_SSL                    | solr.port.ssl                     | 8443                                       |                                                                                                                                                                                                                                                                                                          |
| SOLR_SSL                         | solr.secureComms                  | https                                      |                                                                                                                                                                                                                                                                                                          |
| ENABLE_CLUSTERING                | alfresco.cluster.enabled          | false                                      |                                                                                                                                                                                                                                                                                                          |
| TOMCAT_SSL_KEYSTORE              |                                   | /opt/alfresco/keystore/ssl.keystore        | Path for the ssl keystore file                                                                                                                                                                                                                                                                           |
| TOMCAT_SSL_KEYSTORE_PASSWORD     |                                   |                                            | Password for the ssl keystore                                                                                                                                                                                                                                                                            |
| TOMCAT_SSL_TRUSTSTORE            |                                   | /opt/alfresco/keystore/ssl.truststore      | Path for the ssl truststore file                                                                                                                                                                                                                                                                         |
| TOMCAT_SSL_TRUSTSTORE_PASSWORD   |                                   |                                            | Password for the ssl truststore                                                                                                                                                                                                                                                                          |
| GLOBAL_\<variable\>              | \<variable\>                      |                                            |                                                                                                                                                                                                                                                                                                          |
| TOMCAT_PORT                      |                                   | 8080                                       | non SSL port tomcat is listening on                                                                                                                                                                                                                                                                      |
| TOMCAT_PORT_SSL                  |                                   | 8443                                       | SSL port tomcat is listening on                                                                                                                                                                                                                                                                          |
| TOMCAT_SERVER_PORT               |                                   | 8005                                       | Port for server communication                                                                                                                                                                                                                                                                            |
| TOMCAT_MAX_HTTP_HEADER_SIZE      |                                   | 32768                                      | Maximum http header size                                                                                                                                                                                                                                                                                 |
| TOMCAT_MAX_THREADS               |                                   | 200                                        | Maximum number of threads                                                                                                                                                                                                                                                                                |
| JAVA_XMS                         |                                   |                                            | -Xmx                                                                                                                                                                                                                                                                                                     |
| JAVA_XMX                         |                                   |                                            | -Xms                                                                                                                                                                                                                                                                                                     |
| DEBUG                            |                                   | false                                      | -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n                                                                                                                                                                                                                                    |
| JMX_ENABLED                      |                                   | false                                      | -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote.port=5000 -Djava.rmi.server.hostname=$JMX_RMI_HOST |
| JMX_RMI_HOST                     |                                   | 0.0.0.0                                    |                                                                                                                                                                                                                                                                                                          |
| JAVA_OPTS_\<variable\>=\<value\> |                                   | \<value\>                                  | \<variable\>                                                                                                                                                                                                                                                                                             |
| JSON_LOGGING                     |                                   | false                                      | When true, all logs will be in JSON.                                                                                                                                                                                                                                                                     |
| ACCESS_LOGGING                   |                                   | false                                      | When true, access logs will be printed. These logs are always in JSON format                                                                                                                                                                                                                             |


## Support & Collaboration

These images are updated via pull requests to the [xenit-eu/docker-alfresco/](https://github.com/xenit-eu/docker-alfresco/) Github-repository.

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

If you have access to [Alfresco private repository](https://artifacts.alfresco.com/nexus/content/groups/private/) add the repository to build.gradle and add
```
-Penterprise
```
to your build commands.