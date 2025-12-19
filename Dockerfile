# --- Stage 1: Build the Application ---
# We use a Maven image to build the app from source code
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the application (skipping tests to avoid DB connection errors during build)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
# We use a lightweight Java image to run the app
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Copy the JAR file built in Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Render usually uses 8080 by default)
EXPOSE 8080

# The command to start the application
ENTRYPOINT ["java","-jar","/app.jar"]