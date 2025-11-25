# --- Stage 1: Build the application artifact using Maven ---
# Uses a JDK image (with Maven installed) to compile and package the app
FROM maven:3.9.6-eclipse-temurin-21 AS maven_build

# Set the working directory inside this build container
WORKDIR /app

# Copy the pom.xml file first to allow Docker to cache this layer (efficient build caching)
COPY pom.xml .

# Download dependencies (if pom.xml hasn't changed, this step is skipped in future builds)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Run the clean package command to create the JAR file in the target directory
RUN mvn clean package
#-DskipTests
# --- Stage 2: Run the application using a minimal JRE ---
# Uses a minimal JRE image (no Maven) for the final, smaller runtime image
FROM eclipse-temurin:21-jre-alpine

# Define a build argument for the application version
# A default value is provided for convenience if the ARG isn't passed during build
ARG APP_VERSION=0.0.1-SNAPSHOT

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage (maven_build) into this runtime stage
COPY --from=maven_build /app/target/customer-banking-app-${APP_VERSION}.jar /app/customer-banking-app.jar

# Expose the port your Spring app runs on (8080 by default)
EXPOSE 8080

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "customer-banking-app.jar"]
