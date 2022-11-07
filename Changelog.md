# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/).

## unreleased

### Added

* [PR #98](https://github.com/xenit-eu/docker-alfresco/pull/98) XM2C-43 Merge docker-share into docker-alfresco
* [PR #88](https://github.com/xenit-eu/docker-alfresco/pull/88) DOCKER-415 Add a healthcheck for Alfresco
* [PR #86](https://github.com/xenit-eu/docker-alfresco/pull/86) DOCKER-414 Fix alf_data content store permissions
* [PR #85](https://github.com/xenit-eu/docker-alfresco/pull/85) DOCKER-412 Add default legacy alfrescoConfiguration for solr ssl
* [PR #84](https://github.com/xenit-eu/docker-alfresco/pull/82) XM2C-36: Save alfresco-global.properties to file and add to context.
* [PR #82](https://github.com/xenit-eu/docker-alfresco/pull/82) DOCKER-409: Fix publishing problems, simplify the project structure.
* [PR #79](https://github.com/xenit-eu/docker-alfresco/pull/79) DOCKER-405: Switch to embedded tomcat.
  * Remove dependencies on custom base images
  * Remove Alfresco versions < 6.1
  * Drop legacy images
  * Move to GA for all build, including enterprise
  * Based on eclipse-temurin
  * Support for JSON logging
  * Support for access logging (in JSON)
  * Logging to stdout
* [PR #78](https://github.com/xenit-eu/docker-alfresco/pull/78) DOCKER-408: Add curl timeout to healthcheck commands
* [PR #74](https://github.com/xenit-eu/docker-alfresco/pull/74) DOCKER-406 Added Alfresco 7.2 to subprojects
* [PR #76](https://github.com/xenit-eu/docker-alfresco/pull/76) Allow manual triggers of workflow

### Fixed

* [PR #78](https://github.com/xenit-eu/docker-alfresco/pull/78) DOCKER-408 Add timeout to health check commands

## 2022-02.03 (2022-02-03)

### Added

* [PR #73](https://github.com/xenit-eu/docker-alfresco/pull/73) DOCKER-402 Added Alfresco 7.1 to subprojects

## 2021-04.1 (2021-04-16)

### Fixed

* [PR #69](https://github.com/xenit-eu/docker-alfresco/pull/69) DOCKER-372 Exclude directories left behind when a
  subproject was renamed

### Changed

* [PR #66](https://github.com/xenit-eu/docker-alfresco/pull/66) DOCKER-376 Changed WORKDIR

## 2021-03.1 (2021-03-31)

### Changed

* [PR #65](https://github.com/xenit-eu/docker-alfresco/pull/65) DOCKER-373 Add build for alfresco 7.0.0 images; enhanced
  support for keystore env variables

## 2021-02.1 (2021-02-18)

### Changed

* [PR #62](https://github.com/xenit-eu/docker-alfresco/pull/62) DOCKER-368 Add build for alfresco 6.2.2 images; pull pg
  and solr images from xenit repository public section to avoid dockerhub rate limits

## 2020-09.4 (2020-09-29)

### Changed

* [PR #50](https://github.com/xenit-eu/docker-alfresco/pull/50) DOCKER-311 Change requirements for `chown`
  in `90-init-alfresco.sh` to prevent costly chown operations

## 2020-09.3 (2020-09-21)

### Removed

* [PR #49](https://github.com/xenit-eu/docker-alfresco/pull/49) DOCKER-345 Disabled building of legacy Alfresco
  enterprise 5.0 images

## 2020-09.2 (2020-09-16)

### Fixed

* [PR #48](https://github.com/xenit-eu/docker-alfresco/pull/48) [Issue47](https://github.com/xenit-eu/docker-alfresco/issues/47)
  Fix ambiguity in envvar handling in java initscript

## 2020-09.1 (2020-09-15)

### Added

* [PR #46](https://github.com/xenit-eu/docker-alfresco/pull/46) DOCKER-344 Enabled building of legacy Alfresco
  enterprise 5.0 images

## 2020-09.0 (2020-09-01)

### Added

* [PR #44](https://github.com/xenit-eu/docker-alfresco/pull/44) [Issue43](https://github.com/xenit-eu/docker-alfresco/issues/43)
  DOCKER-341 Added alfrescoConfiguration for custom ssl key- & truststores

## 2020-07.0 (2020-07-03)

### Fixed

* [PR #41](https://github.com/xenit-eu/docker-alfresco/pull/41) DOCKER-339 Init script was installing xmlstarlet and
  required internet connection

## 2020-05.1 (2020-05-29)

### Fixed

* [PR #38](https://github.com/xenit-eu/docker-alfresco/pull/38) DOCKER-335 Fix bug where `SOLR_SSL=none` deleted too
  much from server.xml

## 2020-05.0 (2020-05-26)

### Changed

* [PR #37](https://github.com/xenit-eu/docker-alfresco/pull/37) Update to last 5.2 (5.2.7.3 - added), 6.0 (6.0.1.3),
  6.1 (6.1.1.3) and 6.2 (6.2.1 - added) versions

## 2020-04.0 (2020-04-17)

### Fixed

* [PR #34](https://github.com/xenit-eu/docker-alfresco/pull/34) DOCKER-321 Fix issues that the Tomcat SSL connector no
  longer configured since upgrade to latest version

## 2020-03.0 (2020-03-13)

### Added

* [PR #33](https://github.com/xenit-eu/docker-alfresco/pull/33) DOCKER-312 Start a release procedure: add Xenit version
  to the images tags

## 2020-02.1 (2020-02-24)

### Changed

* [PR #30](https://github.com/xenit-eu/docker-alfresco/pull/30) DOCKER-309 Update Alfresco Enterprise 6.1.0 to 6.1.1

## 2020-02.0 (2020-02-10)

### Added

* [PR #28](https://github.com/xenit-eu/docker-alfresco/pull/28) DOCKER-284 Allow setting log levels through
  docker-compose

## 2019-12.1 (2019-12-05)

### Added

* [PR #25](https://github.com/xenit-eu/docker-alfresco/pull/25) DOCKER-294 Install fontconfig in the skeleton image

## 2019-12.0 (2019-12-02)

### Changed

* [PR #24](https://github.com/xenit-eu/docker-alfresco/pull/24) DOCKER-292 Bump versions transformers. Use transform
  router + queue based transformations.

## 2019-11.2 (2019-11-29)

### Added

* [PR #23](https://github.com/xenit-eu/docker-alfresco/pull/23) DOCKER-291 Added enterprise 6.2.0 and community 6.2.0-ga

## 2019-11.1 (2019-11-28)

### Fixed

* [PR #22](https://github.com/xenit-eu/docker-alfresco/pull/22) DOCKER-288 Make sure java executable points to
  AdoptOpenJDK

## 2019-11.0 (2019-11-05)

### Changed

* [PR #17](https://github.com/xenit-eu/docker-alfresco/pull/17) DOCKER-278 Move java specific variables (jmx, debug,
  memory settings) to java layer

## 2019-09.0 (2019-09-13)

### Added

* [PR #13](https://github.com/xenit-eu/docker-alfresco/pull/13) DOCKER-266 Added enterprise 5.2.6

### Changed

* [PR #11](https://github.com/xenit-eu/docker-alfresco/pull/11) DOCKER-263, DOCKER-259, DOCKER-258, DOCKER-257,
  DOCKER-256 Refactorings, better handling of failures, notifications
    * Increase number of tries in healthcheck
    * Use smartCopy functionality
    * Disable pulling image when using an image id

### Removed

* [PR #14](https://github.com/xenit-eu/docker-alfresco/pull/14) DOCKER-267 Removed enterprise 5.1.1, 5.2.0, 5.2.2 and
  5.2.3

## [v1.0.1] - 2019-08-30

### Changed

* [DOCKER-236] Separate out share: see https://github.com/xenit-eu/docker-share
* [DOCKER-255] Continue to build some legacy images, with both Alfresco and Share inside
* [DOCKER-253] Move handling of properties to Java code instead of shell in the init

### Fixed

* [DOCKER-251] Init script was duplicating JAVA_OPTS_<var> variables

## [v1.0.0] Make it public

### Changed

* [DOCKER-234] Remove dependency on remote jod converter. Separate Dockerfile, init script and docker-compose files for
  Alfresco < 6 (with transformation tools included) and Alfresco >= 6 (using external transformations).
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
* [DOCKER-147] Remove client-specific share alfrescoConfiguration
* [DOCKER-178] Always do compose down to avoid dangling containers

### Changed

* [DOCKER-153] Remove gpg verification of the gosu binary
* [DEVEM-136] [DEVEM-360] Update versions of Gradle and docker-alfresco plugin
* [DOCKER-129] Move the init script in /entrypoint.d and use java's ENTRYPOINT and tomcat's CMD

### Added

* [DOCKER-179] Image for 6.0.1.2
* [DOCKER-181] Possibility to run multiple tests, by using nested configurations. Improved readability by using
  overlayed docker-compose files
* [DOCKER-186] Image for 5.2.5

## [v0.0.8] - 2018-11-30

###

* [DOCKER-138] Removed bundled images.

## [v0.0.7] - 2018-10-30

###

* [DOCKER-137] Download artifacts via gradle instead of via dockerfiles. Vault integration is kept as an example for the
  bundled images only.

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
* [DOCKER-12] [DOCKER-80] [DOCKER-79] [DOCKER-61] [DOCKER-55] [DOCKER-28] Refactorings: created global resources (for
  dockerfiles - with arguments, keystore, init), enterprise + community specific properties, single build.gradle, better
  namings
* [DOCKER-66] Adapted solr images names
* [DOCKER-31] [DOCKER-38] Tomcat-specific variables implemented as JAVA_OPTS and renamed into TOMCAT_<variable> (e.g.
  TOMCAT_PORT, TOMCAT_MAX_THREADS).
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
	
