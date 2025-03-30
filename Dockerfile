# Use Eclipse Temurin (Java 21) as base image for the build stage
FROM eclipse-temurin:21-jdk AS build

# Install Gradle manually in the build stage
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://services.gradle.org/distributions/gradle-8.13-bin.zip \
    && unzip gradle-8.13-bin.zip -d /opt/ \
    && ln -s /opt/gradle-8.13/bin/gradle /usr/bin/gradle \
    && rm gradle-8.13-bin.zip

# Set the working directory in the container
WORKDIR /app

# Copy Gradle build files
COPY build.gradle.kts settings.gradle.kts ./

# Copy the source code
COPY src ./src

# Build the application JAR file
RUN gradle clean bootJar

# Use Eclipse Temurin (Java 21) for the runtime stage
FROM eclipse-temurin:21-jdk AS runtime

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR from the build stage to the runtime container
COPY --from=build /app/build/libs/*.jar app.jar

# Expose a default port (optional)
EXPOSE 8080

# Set environment variable for port configuration
ENV SERVER_PORT=8080

# Run the Spring Boot application with custom port
ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]
