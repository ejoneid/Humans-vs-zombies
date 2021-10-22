FROM maven:3.8.3-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip


FROM openjdk:17-alpine
COPY --from=build /home/app/target/hvz-0.0.1-SNAPSHOT.jar /app/backend.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]