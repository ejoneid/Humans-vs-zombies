FROM openjdk:17-alpine

COPY target/hvz-0.0.1-SNAPSHOT.jar /app/backend.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "backend.jar"]