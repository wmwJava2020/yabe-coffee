FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8013

COPY target/coffee-account-0.0.1-SNAPSHOT.jar coffee-account-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "coffee-account-0.0.1-SNAPSHOT.jar"]
