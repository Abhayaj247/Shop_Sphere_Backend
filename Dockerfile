# ========== Stage 1: Build the JAR ==========
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy your project files
COPY . .

# Build the project using Maven Wrapper or Maven
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests


# ========== Stage 2: Run the application ==========
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Create config directory
RUN mkdir -p /app/config

# Copy your application.properties
COPY src/main/resources/application.properties /app/config/application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.properties"]
