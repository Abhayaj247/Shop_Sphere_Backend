# ========== Stage 1: Build the JAR ==========
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy everything into the build container
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests


# ========== Stage 2: Run the Application ==========
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the final JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
