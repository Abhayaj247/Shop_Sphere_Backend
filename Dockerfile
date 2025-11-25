# Use OpenJDK 17 on Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Spring Boot jar
COPY target/shop_sphere-0.0.1-SNAPSHOT.jar app.jar

# Copy your application.properties to a config folder
RUN mkdir -p /app/config
COPY src/main/resources/application.properties /app/config/application.properties

# Expose port
EXPOSE 8080

# Start the application and tell Spring where to find the external config
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.properties"]
