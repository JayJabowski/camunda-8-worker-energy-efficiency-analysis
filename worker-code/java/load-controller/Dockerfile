# Set Base Image
FROM openjdk:17

# Set Workdir
WORKDIR /app

# Copy jar from home sys into container
COPY target/load-controller-0.0.1-SNAPSHOT.jar /app/load-controller-0.0.1-SNAPSHOT.jar

# run jar
CMD ["java", "-jar", "load-controller-0.0.1-SNAPSHOT.jar"]

