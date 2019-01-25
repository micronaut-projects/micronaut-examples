FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD ./wait-for -t 60 $CONSUL_HOST:$CONSUL_PORT -- \
    ./wait-for -t 60 $REDIS_HOST:$REDIS_PORT -- \
    ./wait-for -t 60 pets:8080 -- \
        java ${JAVA_OPTS} -jar offers-all.jar
COPY build/libs/*-all.jar offers-all.jar
COPY wait-for wait-for