# ===== Stage 1: Build JAR =====
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy project files
COPY . .

# Give execute permission to mvnw (important for Render)
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests


# ===== Stage 2: Run the Application =====
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the final JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
