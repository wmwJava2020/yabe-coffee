FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8012

ADD target/coffee-account-0.0.1-SNAPSHOT.jar coffee-account-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/coffee-account-0.0.1-SNAPSHOT.jar"]

