# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/).

## 2020-11.0
### Changed
* [2020-11-15] [PR #52](https://github.com/xenit-eu/docker-alfresco/pull/52) Update PostgreSQL JDBC driver to 42.2.18

## Unreleased
### Added
* [DOCKER-341] Added configuration for custom ssl key- & truststores
* [DOCKER-266] Added enterprise 5.2.6
* [DOCKER-291] Added enterprise 6.2.0 and community 6.2.0-ga
* [DOCKER-309] Added enterprise 6.1.1
* [DOCKER-294] Install fontconfig in the skeleton image
* [DOCKER-284] Allow setting log levels through docker-compose
* [DOCKER-312] Start a release procedure: add Xenit version to the images tags
	
### Changed
* [DOCKER-311] Change requirements for `chown` in `90-init-alfresco.sh` to prevent costly chown operations
* [DOCKER-278] Move java specific variables (jmx, debug, memory settings) to java layer
* [DOCKER-236] Separate out share: see https://github.com/xenit-eu/docker-share
* [DOCKER-255] Continue to build some legacy images, with both Alfresco and Share inside
* [DOCKER-253] Move handling of properties to Java code instead of shell in the init
* [DOCKER-263], [DOCKER-259], [DOCKER-258], [DOCKER-257], [DOCKER-256] Refactorings, better handling of failures, notifications
* [DOCKER-292] Bump versions transformers. Use transform router + queue based transformations.
* [DOCKER-344] Enabled building of legacy Alfresco enterprise 5.0 images
* [DOCKER-345] Disabled building of legacy Alfresco enterprise 5.0 images
* Increase number of tries in healthcheck
* Upgrade gradle and docker-alfresco plugin
* Use smartCopy functionality
* Disable pulling image when using an image id

### Fixed
* [DOCKER-339] Init script was installing xmlstarlet and required internet connection
* [DOCKER-251] Init script was duplicating JAVA_OPTS_<var> variables
* [DOCKER-288] Make sure java executable points to the Adopt openjdk
* [DOCKER-335] Fix bug where `SOLR_SSL=none` deleted too much from server.xml

## [v1.0.0] Make it public
### Changed
* [DOCKER-234] Remove dependency on remote jod converter. Separate Dockerfile, init script and docker-compose files for Alfresco < 6 (with transformation tools included) and Alfresco >= 6 (using external transformations). 
* [DOCKER-240] Use public images.
* [DOCKER-236] Separate out share.
* [DOCKER-242] Build in function of flavor. Registry name configurable.
* [DOCKER-243] Travis file.

## [v0.0.11] - 2019-07-01
### Added
* [DOCKER-225] Make sure there is one community build for each major-minor version

### Changed
* [DOCKER-192] Remove tomcat-specific handling of variables, it is now done in the tomcat image
* [DOCKER-208] Use an environment variable for tomcat port in the healthcheck

### Fixed
* [DOCKER-205] Use DB_ variables in the connector url
* [DOCKER-241] Fix DEBUG setting when running on Java 9+

## [v0.0.10] - 2019-05-10
### Added
* [DOCKER-182] Init script for share, which edits share-config-custom.xml
* [DOCKER-183] Image for Alfresco enterprise 5.1.5
* [DOCKER-156] Image for Alfresco enterprise 6.1
* [DOCKER-186] Image for Alfresco enterprise 5.2.5

### Changed
* [DOCKER-191] Remove gosu from Alfresco images, catalina si started with gosu in the tomcat images

### Fixed
* [DOCKER-195] Replacement in the init script not ok, restarting container breaks it

## [v0.0.9] - 2019-04-29
### Fixed
* [DOCKER-142] JAVA_OPTS are duplicated when starting tomcat
* [DOCKER-175] Backup location for solr6
* [DOCKER-145] Image of Alfresco 4.2 has MaxPermSize set, otherwise Alfresco does not start properly
* [DOCKER-147] Remove client-specific share configuration	
* [DOCKER-178] Always do compose down to avoid dangling containers
	
### Changed
* [DOCKER-153] Remove gpg verification of the gosu binary
* [DEVEM-136] [DEVEM-360] Update versions of Gradle and docker-alfresco plugin
* [DOCKER-129] Move the init script in /entrypoint.d and use java's ENTRYPOINT and tomcat's CMD
	
### Added
* [DOCKER-179] Image for 6.0.1.2
* [DOCKER-181] Possibility to run multiple tests, by using nested configurations. Improved readability by using overlayed docker-compose files
* [DOCKER-186] Image for 5.2.5
	
## [v0.0.8] - 2018-11-30
###
* [DOCKER-138] Removed bundled images.

## [v0.0.7] - 2018-10-30
###
* [DOCKER-137] Download artifacts via gradle instead of via dockerfiles. Vault integration is kept as an example for the bundled images only.

	
## [v0.0.6] - 2018-10-26
### Added
* [DOCKER-126] Image for community 6.0.7-ga

### Removed
* [DOCKER-128] Remove community image 6.0.0, is unstable


## [v0.0.5] - 2018-09-24
### Changed
* [DOCKER-115] Move healthchecks to Dockerfiles

### Fixed
* [DOCKER-91] Redirect port was not parametrized in server.xml, therefore it was not possible to change it

## [v0.0.4] - 2018-09-10
### Added
* [DOCKER-76] Smoke tests

### Fixed
* [DOCKER-107] Property solr.port was not set due to wrong check in the init.sh

### Removed
* [DOCKER-120] Removed ALFRESCO_PORT and ALFRESCO_PORT_SSL referring to ports in server.xml. 
Correct way to change those - via TOMCAT_PORT and TOMCAT_PORT_SSL.


## [v0.0.3] - 2018-08-28
### Added
* [DOCKER-93] Image for Alfresco Enterprise 6.0
* [DOCKER-102] Image for Alfresco Enterprise 5.2.4 (last SP)

### Fixed
* [DOCKER-101] Fix tag on Alfresco Community 5.2.g

## [v0.0.2] - 2018-07-25
### Changed
* [DOCKER-98] Refactorings: simplified and deduplicated build.gradle, addition of "local" resources 

### Security
* [DOCKER-96] Do not echo properties being replaced, they should not appear in the logs (some are confidential)
	
## [v0.0.1] - 2018-07-19
### Changed
* [DOCKER-82] Good defaults for properties
* [DOCKER-12] [DOCKER-80] [DOCKER-79] [DOCKER-61] [DOCKER-55] [DOCKER-28] Refactorings: created global resources (for dockerfiles - with arguments, keystore, init), enterprise + community specific properties, single build.gradle, better namings
* [DOCKER-66] Adapted solr images names
* [DOCKER-31] [DOCKER-38] Tomcat-specific variables implemented as JAVA_OPTS and renamed into TOMCAT_<variable> (e.g. TOMCAT_PORT, TOMCAT_MAX_THREADS).
* [DOCKER-37] Adapt bundle images for new structure

### Added
* [DOCKER-92] Image for Alfresco Enterprise 5.2.3
* [DOCKER-87] Image for Alfresco Enterprise 4.2.8
* [DOCKER-67] Include jod converter amp
* [DOCKER-57] Added alfresco pdf renderer executable to Alfresco images >=5.2
* [DOCKER-94] Declare growing folders as volumes
* [DOCKER-35] Support for JAVA_OPTS_<variable> variables, allowing for overrides in different docker-compose files  
* [DOCKER-43] First version Changelog

### Removed
* [DOCKER-37] Removed bundled images for versions >=5.0
* [DOCKER-26] Removed the PROXY_<variable> parameters from the init
	
