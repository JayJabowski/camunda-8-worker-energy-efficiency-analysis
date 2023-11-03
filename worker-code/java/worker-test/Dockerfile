# Set Base Image
FROM openjdk:17

# Set Workdir
WORKDIR /app

# Copy jar from home sys into container
COPY target/worker-test-0.0.1-SNAPSHOT.jar /app/worker-test-0.0.1-SNAPSHOT.jar

# run jar
CMD ["java", "-jar", "worker-test-0.0.1-SNAPSHOT.jar"]

