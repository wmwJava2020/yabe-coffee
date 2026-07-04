# Use JRE — lighter image for runtime only
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR — consistent naming, all uppercase SNAPSHOT
COPY target/coffee-house-0.0.1-snapshot.jar app.jar

# Your custom port
EXPOSE 8012

# Health check using your port
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8012/actuator/health || exit 1

# Use 'app.jar' — simple, no case confusion
ENTRYPOINT ["java", "-jar", "app.jar"]
