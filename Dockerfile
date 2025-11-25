# ========== Stage 1: Build ========== 
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the pom and wrapper files first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Ensure mvnw is executable
RUN chmod +x mvnw

# Download dependencies (caching)
RUN ./mvnw dependency:go-offline

# Copy the full project
COPY src src

# Build the jar
RUN ./mvnw clean package -DskipTests


# ========== Stage 2: Run ========== 
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Use Spring Boot externalized config from environment variables
ENTRYPOINT ["java", "-jar", "app.jar"]
