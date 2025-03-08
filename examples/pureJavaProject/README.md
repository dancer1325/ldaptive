# Goal
* test examples of the documentation -- via -- pure java

## How to run it locally?
* | "examples/"
  * `javac -cp "lib/*:." src/Home/Main.java -d .`
  * `java -cp "lib/*:." Main`
    * Problems:
      * Problem1: "java.lang.NoClassDefFoundError: io/netty/util/concurrent/GenericFutureListener"
        * Attempt1: download & add "netty-all-4.1.115.Final.jar"
        * Solution: TODO: