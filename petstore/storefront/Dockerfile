FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD ./wait-for -t 300 $CONSUL_HOST:$CONSUL_PORT -- \
        ./wait-for -t 300 pets:8080 -- \
        ./wait-for -t 300 vendors:8080 -- \
        ./wait-for -t 300 comments:8080 -- \
        echo "All dependencies ready. Starting application..." && \
        java ${JAVA_OPTS} -jar storefront-all.jar
COPY build/libs/*-all.jar storefront-all.jar
COPY wait-for wait-for