# -------------------------------
# Build stage
# -------------------------------
FROM maven:3.9.1-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Copy source code and build the jar
COPY src ./src
RUN mvn -B clean package -DskipTests

# -------------------------------
# Runtime stage
# -------------------------------
FROM eclipse-temurin:17-jdk

# Install postgresql-client (use --no-install-recommends and cleanup)
RUN apt-get update && apt-get install -y --no-install-recommends postgresql-client \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the port Spring Boot uses
EXPOSE 8080

# Start Spring Boot app directly
CMD ["java", "-jar", "app.jar"]