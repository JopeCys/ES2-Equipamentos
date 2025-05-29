FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/nomeMicrosservico.jar nomeMicrosservico.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "nomeMicrosservico.jar"]