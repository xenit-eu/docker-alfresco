# Alfresco in Docker

## Images created 

* [`alfresco-skeleton`] = skeleton common for all images per major version; includes java, tomcat, init script, keystore
* [`alfresco-repository-enterprise`] = enterprise Alfresco images
* [`alfresco-repository-community`] = community Alfresco images

## Supported Tags

* [`:5.1.5`, `:5.1`, `:5`] = major, minor, revision for enterprise
* [`:5.1.e`, `:5.1`] =  major, minor, revision for community

## Overview

This is our repository with _Dockerfile_ s that are the cannonical source for our Alfresco Docker images. 

All images are automatically built by [jenkins-2](https://jenkins-2.xenit.eu) and published to [hub.xenit.eu](https://hub.xenit.eu).
Community images are built by [Travis](https://travis-ci.org/xenit-eu/) and published to [docker hub](https://hub.docker.com/u/xeniteu).

## Image Variants

Multi-container: `alfresco` to be used together with [`docker-share`](https://github.com/xenit-eu/docker-share), [`docker-solr`](https://github.com/xenit-eu/docker-solr), [`postgres`](https://github.com/xenit-eu/docker-postgres).

For transformations [`jodconverter`](https://github.com/xenit-eu/jodconverter) can be used with a derived image containing the amp from [`alfresco-remote-jodconverter`](https://github.com/xenit-eu/alfresco-remote-jodconverter)
Current images work with remote transformers for Alfresco >= 6 and with locally-installed tools for Alfresco < 6.

Note: up to Alfresco 5.1 inclusive, ghoscript was used to handle thumbnails and previews. In our images it is also used on version 5.2.
In 2018 a security update disabled some types in /etc/ImageMagick/policy.xml (see https://launchpad.net/ubuntu/+source/imagemagick/8:6.7.7.10-6ubuntu3.13). 
Errors like:

* convert: not authorized * or * convert: no decode delegate for this image format *

can be solved by commening out following lines at the end of the file /etc/ImageMagick/policy.xml:
```
<!-- disable ghostscript format types -->
  <policy domain="coder" rights="none" pattern="PS" />
  <policy domain="coder" rights="none" pattern="EPS" />
  <policy domain="coder" rights="none" pattern="PDF" />
  <policy domain="coder" rights="none" pattern="XPS" />
```

Note: for the community versions < 6 not all transformations are working when using the tools installed in the images. 
Since in practice the [`remote jod converter`](https://github.com/xenit-eu/jodconverter) can be used, no much effort was invested in making this work. 

### alfresco-repository-enterprise:<version> or alfresco-repository-community:<version>

These are the images used for a multi-container Alfresco enterprise deployment, especially in production.

The most basic setup uses the docker-compose files from [`rc/master/src/integrationTest/resources/`](https://github.com/xenit-eu/docker-alfresco/tree/master/src/integrationTest/resources). These files are used in the integration tests.

In a production setup it is best to use the remote jodconverter for transformations and a proxy container in front of Alfresco and Share.

Note that this uses the compose file [version 3](https://docs.docker.com/compose/compose-file/#version-3) syntax, which means you need [Docker Compose 1.10.0+](https://github.com/docker/compose/releases?after=1.10.1).

Images can be customized further by using environment variables - see section Environment Variables.

## Environment variables

There are several environment variables available to tweak the behaviour. While none of the variables are required, they may significantly aid you in using these images.
The variables are read by an init script which further replaces them in the relevant files. Such relevant files include - for alfresco:

* alfresco-global.properties
* server.xml (ports)
* context.xml (persistent sessions)
* setenv.sh (JAVA_OPTS parameters)

The alfresco-global.properties can be set via a generic mechanism by setting environment variables of the form GLOBAL_<parameter>, e.g. GLOBAL_alfresco.host. 
They can also be set via environment variables of the form JAVA_OPTS_<ignored_key> where the value should be "-Dkey=value".

A subset of the alfresco-global.properties have also dedicated environment variables e.g. SOLR_SSL. Generic variables take precedence.

A subset of java variables have also dedicated environment variables e.g. JAVA_XMX. Generic variables take precedence.

Environment variables:

| Variable                    | alfresco-global.property variable | java variable                                                | Default                                                      | Comments |
| --------------------------- | --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | --------------------------- |
| SOLR_SSL                    | solr.secureComms                  |                                                              | https                                                        | disabling only works for Alfresco>=5.1 |
| ALFRESCO_HOST               | alfresco.host                     |                                                              | alfresco                                                    |  |
| ALFRESCO_PORT               | alfresco.port                     |                                                              | 8080                                                         |  |
| ALFRESCO_PROTOCOL           | alfresco.protocol                 |                                                              | http                                                         |  |
| SHARE_HOST                  | share.host                        |                                                              | localhost                                                    |  |
| SHARE_PORT                  | share.port                        |                                                              | 8080                                                         |  |
| SHARE_PROTOCOL              | share.protocol                    |                                                              | http                                                         |  |
| DB_DRIVER                   | db.driver                         |                                                              | org.postgresql.Driver                                        |  |
| DB_HOST                     | db.host                           |                                                              | localhost                                                    |  |
| DB_PORT                     | db.port                           |                                                              | 5432                                                         |  |
| DB_NAME                     | db.name                           |                                                              | alfresco                                                     |  |
| DB_USERNAME                 | db.username                       |                                                              | alfresco                                                     |  |
| DB_PASSWORD                 | db.password                       |                                                              | admin                                                        |  |
| DB_URL                      | db.url                            |                                                              | jdbc:postgresql://postgresql:5432/alfresco                   |  |
| DB_QUERY                    | db.pool.validate.query            |                                                              | select 1                                                     |  |
| INDEX                       | index.subsystem.name              |                                                              | solr for alfresco 4 <br>solr4 for alfresco 5<br>solr6 for alfresco >=5.2 |  |
|                             | solr.host                         |                                                              | solr                                                         |  |
| SOLR_PORT                   | solr.port                         |                                                              | 8080                                                         |  |
| SOLR_PORT_SSL               | solr.port.ssl                     |                                                              | 8443                                                         |  |
| DYNAMIC_SHARD_REGISTRATION  | solr.useDynamicShardRegistration  |                                                              | false                                                        |  |
| MAIL_HOST                   | mail.host                         |                                                              | localhost                                                    |  |
| ENABLE_CIFS                 | cifs.enabled                      |                                                              | false                                                        |  |
| ENABLE_FTP                  | ftp.enabled                       |                                                              | false                                                        |  |
| ENABLE_CLUSTERING           | alfresco.cluster.enabled          |                                                              | false                                                        |  |
| TOMCAT_PORT                 |                                   | -DTOMCAT_PORT                                                | 8080                                                         |  |
| TOMCAT_PORT_SSL             |                                   | -DTOMCAT_PORT_SSL                                            | 8443                                                         |  |
| TOMCAT_AJP_PORT             |                                   | -DTOMCAT_AJP_PORT                                            | 8009                                                         |  |
| TOMCAT_SERVER_PORT          |                                   | -DTOMCAT_SERVER_PORT                                         | 8005                                                         |  |
| TOMCAT_MAX_HTTP_HEADER_SIZE |                                   | -DTOMCAT_MAX_HTTP_HEADER_SIZE  or -DMAX_HTTP_HEADER_SIZE                              | 32768                                                        |  |
| TOMCAT_MAX_THREADS          |                                   | -DTOMCAT_MAX_THREADS or -DMAX_THREADS                                              | 200                                                          |  |
| JAVA_XMS                    |                                   | -Xmx                                                         |                                                              |  |
| JAVA_XMX                    |                                   | -Xms                                                         |                                                              |  |
| DEBUG                       |                                   | -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n |     false                                                         |  |
| JMX_ENABLED                 |                                   | -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote.port=5000 -Djava.rmi.server.hostname=$JMX_RMI_HOST |     false                                                         |  |
| JMX_RMI_HOST                |                                   |                                                              |  0.0.0.0                                                            |  |
| GLOBAL_\<variable\>           | \<variable\>                        |                                                              |                                                              |  |
| JAVA_OPTS_\<variable\>=\<value\>       |                                   | \<value\>                                                   |                                                              |  |

## Support & Collaboration

These images are updated via pull requests to the [xenit-eu/docker-alfresco/](https://github.com/xenit-eu/docker-alfresco/) Github-repository.

**Maintained by:**

Roxana Angheluta <roxana.angheluta@xenit.eu>, Lars Vierbergen <lars.vierbergen@xenit.eu>

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

If you have access to [Alfresco private repository](https://artifacts.alfresco.com/nexus/content/groups/private/) add the repository to build.gradle and add -Penterprise to your build commands.

## FAQ

### How do I access the Tomcat debugport ?

Set the environment variable DEBUG=true. The debug port is 8000.

### How do I enable JMX?

Set the environment variable JMX_ENABLED=true. Jmx port is 5000.


