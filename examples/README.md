# Goal
* test examples of the documentation

## How to run a ldap server locally?
* `docker run -p 389:389 -p 636:636 --name openldap osixia/openldap:latest`
  * see
    * [docker openldap](https://github.com/osixia/docker-openldap?tab=readme-ov-file#quick-start) &
    * [openldap](https://www.openldap.org/) 

## ways
### Pure Java project
* == NO dependency manager
* [here](pureJavaProject)
### via Maven
* [here](example)