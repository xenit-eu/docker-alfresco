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
* [`alfresco-enterprise`] = enterprise legacy Alfresco images, with repository + share
* [`alfresco-community`] = community legacy Alfresco images, with repository + share

## Supported Tags

* [`:5.1.5`, `:5.1`, `:5`] = major, minor, revision for enterprise
* [`:5.1.e`, `:5.1`] =  major, minor, revision for community

## Overview

This is our repository with _Dockerfile_ s that are the cannonical source for our Alfresco Docker images. 

All images are automatically built by [jenkins-2](https://jenkins-2.xenit.eu) and published to [hub.xenit.eu](https://hub.xenit.eu).
Community images are built by [Travis](https://travis-ci.org/xenit-eu/) and published to [docker hub](https://hub.docker.com/u/xenit).

## Image Variants

### Repository-only:
* alfresco-repository-enterprise:<version>
* alfresco-repository-community:<version>

These are the images used for a multi-container Alfresco enterprise deployment in production. To be used together with [`docker-share`](https://github.com/xenit-eu/docker-share), [`docker-solr`](https://github.com/xenit-eu/docker-solr), [`postgres`](https://github.com/xenit-eu/docker-postgres).

The most basic setup uses the docker-compose files from [`2repository/src/integrationTest/resources`](https://github.com/xenit-eu/docker-alfresco/tree/master/2repository/src/integrationTest/resources). These files are used in the integration tests.

### Repository and share:
* alfresco-enterprise:<version>
* alfresco-community:<version>

These are legacy images containing both repository and share. Not all versions are built.

## Transformations
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

Images can be customized further by using environment variables - see section Environment Variables.


## Environment variables

There are several environment variables available to tweak the behaviour. While none of the variables are required, they may significantly aid you in using these images.

See also environment variables from lower layers: [`docker-openjdk`](https://github.com/xenit-eu/docker-openjdk) and [`docker-tomcat`](https://github.com/xenit-eu/docker-tomcat).

The variables are read by an init script which further replaces them in the relevant files. Such relevant files include:

* alfresco-global.properties

The alfresco-global.properties can be set via a generic mechanism by setting environment variables of the form GLOBAL_<parameter>, e.g. GLOBAL_alfresco.host. 

A subset of the alfresco-global.properties have also dedicated environment variables e.g. SOLR_SSL. Generic variables take precedence.


* TLS with Custom keystores

It is advisable when enabling TLS to use newly create key- and truststores for keeping your keys and certificates. These stores should follow
conventions as set out in the [alfresco documentation](https://docs.alfresco.com/search-enterprise/concepts/generate-keys-overview.html), but their location can be changed with the `DIR_KEYSTORE` environment variable.
The system and application are provided access to these stores by adding a store-password.properties file for each respective store in the store 
directory (see Alfresco documentation), and requires the environment variable `CUSTOM_KEYSTORES` to be set to true. 

* Log4j properties

It is possible to set the loglevel for a given log4j logger by adding entries of the format`LOG4J_logger.fully.qualified.classname=LOGLEVEL`. The first step on the property path (`log4j`) will be added by the script.
**note:**
The current implementation will break generation of a custom image where the unexploded alfresco war is `COPY`ed into the image via Dockerfile ([See issue 32](https://github.com/xenit-eu/docker-alfresco/issues/32)).
This issue can be worked around by `COPY`ing the exploded war, or by using the [alfresco-docker-gradle-plugin](https://github.com/xenit-eu/alfresco-docker-gradle-plugin).

Environment variables:

| Variable                    | alfresco-global.property variable | java variable                                                | Default                                                      | Comments |
| --------------------------- | --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | --------------------------- |
| DIR_ROOT                    | dir.root                          |                                                              | /opt/alfresco/alf_data                                                        |  |
| DIR_KEYSTORE                | dir.keystore                      |                                                              | /opt/alfresco/keystore                                                        |  |
| CUSTOM_KEYSTORES            | N/A                               |                                                              | false                                                        | Triggers whether during init the system will attempt to fill in password values into the tomcat connector definition from the store-password.properties files |
| ALFRESCO_HOST               | alfresco.host                     |                                                              | alfresco                                                    |  |
| ALFRESCO_PORT               | alfresco.port                     |                                                              | 8080                                                         |  |
| ALFRESCO_PROTOCOL           | alfresco.protocol                 |                                                              | http                                                         |  |
| ALFRESCO_CONTEXT            | alfresco.context                  |                                                              | alfresco                                                         |  |
| SHARE_HOST                  | share.host                        |                                                              | share                                                    |  |
| SHARE_PORT                  | share.port                        |                                                              | 8080                                                         |  |
| SHARE_PROTOCOL              | share.protocol                    |                                                              | http                                                         |  |
| SHARE_CONTEXT               | share.context                     |                                                              | share                                                        |  |
| DB_DRIVER                   | db.driver                         |                                                              | org.postgresql.Driver                                        |  |
| DB_HOST                     | db.host                           |                                                              | localhost                                                    |  |
| DB_PORT                     | db.port                           |                                                              | 5432                                                         |  |
| DB_NAME                     | db.name                           |                                                              | alfresco                                                     |  |
| DB_USERNAME                 | db.username                       |                                                              | alfresco                                                     |  |
| DB_PASSWORD                 | db.password                       |                                                              | admin                                                        |  |
| DB_URL                      | db.url                            |                                                              | jdbc:postgresql://postgresql:5432/alfresco                   |  |
| DB_QUERY                    | db.pool.validate.query            |                                                              | select 1                                                     |  |
| INDEX                       | index.subsystem.name              |                                                              | solr for alfresco 4 <br>solr4 for alfresco 5<br>solr6 for alfresco >=5.2 |  |
| SOLR_HOST                   | solr.host                         |                                                              | solr                                                         |  |
| SOLR_PORT                   | solr.port                         |                                                              | 8080                                                         |  |
| SOLR_PORT_SSL               | solr.port.ssl                     |                                                              | 8443                                                         |  |
| DYNAMIC_SHARD_REGISTRATION  | solr.useDynamicShardRegistration  |                                                              | false                                                        |  |
| SOLR_SSL                    | solr.secureComms                  |                                                              | https                                                        | disabling only works for Alfresco>=5.1 |
| MAIL_HOST                   | mail.host                         |                                                              | localhost                                                    |  |
| ENABLE_CIFS                 | cifs.enabled                      |                                                              | false                                                        |  |
| ENABLE_FTP                  | ftp.enabled                       |                                                              | false                                                        |  |
| ENABLE_CLUSTERING           | alfresco.cluster.enabled          |                                                              | false                                                        |  |
| ENCRYPTION_KEYSTORE_TYPE    | encryption.keystore.type          |                                                              | JCEKS                                                        |  |
| ENCRYPTION_CIPHERALGORITHM  | encryption.cipherAlgorithm        |                                                              | DESede/CBC/PKCS5Padding                                      |  |
| ENCRYPTION_KEYALGORITHM     | encryption.keyAlgorithm           |                                                              | DESede                                                       |  |
| ENCRYPTION_KEYSTORE_LOCATION| encryption.keystore.location      |                                                              | /opt/alfresco/keystore/keystore                              |  |
| METADATA_KEYSTORE_PASSWORD  |                                   | metadata-keystore.password                                   | mp6yc0UD9e                                                   |  |
| METADATA_KEYSTORE_ALIASES   |                                   | metadata-keystore.aliases                                    | metadata                                                     |  |
| METADATA_KEYSTORE_${alias}_PASSWORD  |                          | metadata-keystore.${alias}.password                          | oKIWzVdEdA (for default alias `metadata`)                    |  |
| METADATA_KEYSTORE_${alias}_ALGORITHM |                          | metadata-keystore.${alias}.algorithm                         | DESede (for default alias `metadata`)                        |  |
| SSL_KEYSTORE_PASSWORD       |                                   | ssl-keystore.password                                        | kT9X6oe68t                                                   |  |
| SSL_KEYSTORE_ALIASES        |                                   | ssl-keystore.aliases                                         | ssl.alfresco.ca, ssl.repo                                    |  |
| SSL_KEYSTORE_${alias}_PASSWORD       |                          | ssl-keystore.${alias}.password                               | kT9X6oe68t (for default alias `ssl.alfresco.ca`)             |  |
| SSL_TRUSTSTORE_PASSWORD     |                                   | ssl-truststore.password                                      | kT9X6oe68t                                                   |  |
| SSL_TRUSTSTORE_ALIASES      |                                   | ssl-truststore.aliases                                       | alfresco.ca                                                  |  |
| SSL_TRUSTSTORE_${alias}_PASSWORD     |                          | ssl-truststore.${alias}.password                             | kT9X6oe68t (for default alias `alfresco.ca`)                 |  |
| GLOBAL_\<variable\>         | \<variable\>                      |                                                              |                                                              |  |
| LOG4J_\<property-path\>     | N/A                               | N/A                                                          | N/A                                                          | Add the given property path and value to the alfresco log4j properties file. |

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

To build a legacy image add
```
-Plegacy
```
to your build commands.
