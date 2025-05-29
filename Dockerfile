FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/microsservico.jar microsservico.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "microsservico.jar"]