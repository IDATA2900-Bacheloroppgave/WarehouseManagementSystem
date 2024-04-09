# First stage: Maven or Gradle build
# Using Maven's official image, based on openjdk 17
FROM maven:3.8.4-openjdk-17-slim as build

# Copy the project files to the container
COPY . /home/app

# Set the working directory
WORKDIR /home/app

# Package the application
RUN mvn clean package -DskipTests

# Second stage: Create the final Docker image with just the JAR file
FROM openjdk:17-slim

# Copy the JAR file from the previous stage
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]
