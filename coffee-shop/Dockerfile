FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8012

COPY target/coffee-house-0.0.1-SNAPSHOT.jar coffee-house-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "coffee-house-0.0.1-SNAPSHOT.jar"]
