# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Create necessary directories
RUN mkdir -p /app/cities-storage
RUN mkdir -p /app/output

# Expose port 8080 to allow external access
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]