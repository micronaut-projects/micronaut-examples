./gradlew assemble
native-image --no-server --no-fallback --class-path build/libs/example-*-all.jar
