# Alfresco in Docker

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

The most basic setup uses the docker-compose files from [`src/master/src/integrationTest/resources/`](https://github.com/xenit-eu/docker-alfresco/tree/master/src/integrationTest/resources). These files are used in the integration tests.

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
The variables are read by an init script which further replaces them in the relevant files. Such relevant files include:

* alfresco-global.properties

The alfresco-global.properties can be set via a generic mechanism by setting environment variables of the form GLOBAL_<parameter>, e.g. GLOBAL_alfresco.host. 

A subset of the alfresco-global.properties have also dedicated environment variables e.g. SOLR_SSL. Generic variables take precedence.

See also environment variables from lower layers: [`docker-openjdk`](https://github.com/xenit-eu/docker-openjdk) and [`docker-tomcat`](https://github.com/xenit-eu/docker-tomcat).

Environment variables:

| Variable                    | alfresco-global.property variable | java variable                                                | Default                                                      | Comments |
| --------------------------- | --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | --------------------------- |
| DIR_ROOT                    | dir.root                          |                                                              | /opt/alfresco/alf_data                                                        |  |
| DIR_KEYSTORE                | dir.keystore                      |                                                              | /opt/alfresco/keystore                                                        |  |
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
| SOLR_HOST             | solr.host                         |                                                              | solr                                                         |  |
| SOLR_PORT                   | solr.port                         |                                                              | 8080                                                         |  |
| SOLR_PORT_SSL               | solr.port.ssl                     |                                                              | 8443                                                         |  |
| DYNAMIC_SHARD_REGISTRATION  | solr.useDynamicShardRegistration  |                                                              | false                                                        |  |
| SOLR_SSL                    | solr.secureComms                  |                                                              | https                                                        | disabling only works for Alfresco>=5.1 |
| MAIL_HOST                   | mail.host                         |                                                              | localhost                                                    |  |
| ENABLE_CIFS                 | cifs.enabled                      |                                                              | false                                                        |  |
| ENABLE_FTP                  | ftp.enabled                       |                                                              | false                                                        |  |
| ENABLE_CLUSTERING           | alfresco.cluster.enabled          |                                                              | false                                                        |  |
| GLOBAL_\<variable\>           | \<variable\>                        |                                                              |                                                              |  |


## Support & Collaboration

These images are updated via pull requests to the [xenit-eu/docker-alfresco/](https://github.com/xenit-eu/docker-alfresco/) Github-repository.

**Maintained by:**

Roxana Angheluta <roxana.angheluta@xenit.eu>

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
