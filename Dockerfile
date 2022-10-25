FROM adoptopenjdk/openjdk11:alpine

COPY ./target/dual-validate-*.jar /app/dual-validate.jar
COPY ./jsonsample /app/jsonsample

WORKDIR /app

RUN  set -eux; \
     addgroup demo && adduser -DH -G demo demo; \
     wget   https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.27.0/elastic-apm-agent-1.27.0.jar    -P  /app/  ; \
     wget   https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.28.3/elastic-apm-agent-1.28.3.jar    -P  /app/  ; \
     chown -R demo:demo /app ; \
     ln -snf /usr/share/zoneinfo/Asia/Taipei /etc/localtime && echo Asia/Taipei > /etc/timezone

USER demo

EXPOSE 8080/tcp

ENV TZ=Asia/Taipei
ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS    -jar  /app/dual-validate.jar"]