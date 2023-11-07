# Set Base Image
FROM openjdk:17

# Set Workdir
WORKDIR /app

# Copy jar from home sys into container
COPY target/rest-worker-0.0.1-SNAPSHOT.jar /app/rest-worker-0.0.1-SNAPSHOT.jar

# run jar
CMD ["java", "-jar", "rest-worker-0.0.1-SNAPSHOT.jar"]

