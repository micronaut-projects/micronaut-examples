FROM openjdk:8-jre-alpine

RUN addgroup -S app && adduser -S -g app app

WORKDIR /app
COPY build/libs/*-all.jar /app/Handler.jar

RUN apk --no-cache add curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && cp /usr/bin/fwatchdog /home/app \
    && apk del curl --no-cache

USER app

ENV fprocess="xargs java -noverify -XX:TieredStopAtLevel=1 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar Handler.jar -d"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
