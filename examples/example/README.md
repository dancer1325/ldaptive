# Goal
* test examples of the documentation -- via -- maven

## How to run it locally?
### IDE
* Run App.main
### Commands
* `mvn clean compile`
* `java -classpath target/classes/ com.App`
  * Problems:
    * Problem1: "java.lang.NoClassDefFoundError: org/ldaptive/LdapException"
      * Note: | "target/classes/", NOT found required libraries
      * Solution: TODO: