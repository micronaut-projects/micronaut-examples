FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD ./wait-for -t 60 $CONSUL_HOST:$CONSUL_PORT -- \
    ./wait-for -t 60 $MONGO_HOST:$MONGO_PORT -- \
        echo "All dependencies ready. Starting application..." && \
        java ${JAVA_OPTS} -jar pets-all.jar
COPY build/libs/*-all.jar pets-all.jar
COPY wait-for wait-for