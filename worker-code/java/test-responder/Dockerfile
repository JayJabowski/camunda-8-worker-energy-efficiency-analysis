# Set Base Image
FROM openjdk:17

# Set Workdir
WORKDIR /app

# Copy jar from home sys into container
COPY target/test-responder-0.0.1-SNAPSHOT.jar /app/test-responder-0.0.1-SNAPSHOT.jar

# run jar
CMD ["java", "-jar", "test-responder-0.0.1-SNAPSHOT.jar"]

