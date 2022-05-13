FROM openjdk:17
COPY . /usr/src/standalone-http-server
WORKDIR /usr/src/standalone-http-server
CMD ["java", "StandaloneHttpServer.java"]
