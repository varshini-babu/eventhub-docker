

# Use an official OpenJDK runtime as a parent image
FROM openjdk:24-slim-bullseye

# Set the working directory
WORKDIR /app

# Copy the Maven or Gradle build file to fetch dependencies
COPY ./target/event-hub.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8081

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
