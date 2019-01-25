FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD ./wait-for -t 60 $CONSUL_HOST:$CONSUL_PORT -- \
    java ${JAVA_OPTS} -jar mail-all.jar
COPY build/libs/*-all.jar mail-all.jar
COPY wait-for wait-for