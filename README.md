## Micronaut Examples

This repository contains examples of [Micronaut](http://micronaut.io).

The following examples are featured.

### hello-world-java

The hello-world-java example is a simple Hello World implementation written in Java.

To run the application with Gradle use:

```bash
$ cd hello-world-java
$ ./gradlew run
```

To run the application with Maven use:

```bash
$ ./mvnw exec:exec
```

Then go to http://localhost:8080/hello/John

To run the tests run `./gradlew test` or `/mvnw test`. 

### hello-world-groovy

The `hello-world-groovy` example is a simple Hello World implementation written in Groovy.

To run the application with Gradle use:

```bash
$ cd hello-world-groovy
$ ./gradlew run
```

Then go to http://localhost:8080/hello/John

### hello-world-kotlin


The `hello-world-kotlin` example is a simple Hello World implementation written in Kotlin.

To run the application with Gradle use:

```bash
$ cd hello-world-kotlin
$ ./gradlew run
```

Then go to http://localhost:8080/hello/John

### petstore

The `petstore` example is a Micronaut petstore implementation featuring multiple Microservices and a `docker-compose.yml` file for starting the application.

See the `README` in the root of the `petstore` example for usage instructions.


