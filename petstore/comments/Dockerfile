FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD ./wait-for -t 60 $CONSUL_HOST:$CONSUL_PORT -- \
    ./wait-for -t 60 $NEO4J_HOST:$NEO4J_PORT -- \
        java ${JAVA_OPTS} -jar comments-all.jar
COPY build/libs/*-all.jar comments-all.jar
COPY wait-for wait-for