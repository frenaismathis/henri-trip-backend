# -------------------------------
# Build stage
# -------------------------------
FROM maven:3.9.1-eclipse-temurin-20 AS build

WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Copy source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# -------------------------------
# Runtime stage
# -------------------------------
FROM eclipse-temurin:20-jdk

# Install postgresql-client
RUN apt-get update && apt-get install -y postgresql-client

WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the port Spring Boot uses
EXPOSE 8080

# Start Spring Boot app directly
CMD ["java", "-jar", "app.jar"]