# Dockerfile

# Use an OpenJDK base image
FROM maven:3.8.1-openjdk-16-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/booking-management-system-1.0-SNAPSHOT.jar /app/booking-management-system-1.0-SNAPSHOT.jar

# Copy wait-for-postgres.sh script
COPY wait-for-postgres.sh /app/wait-for-postgres.sh
RUN chmod +x /app/wait-for-postgres.sh

# Specify the command to run your application, using the wait-for script to ensure PostgreSQL is ready
CMD ["/app/wait-for-postgres.sh", "postgres-db:5432", "java", "-jar", "booking-management-system-1.0-SNAPSHOT.jar"]

# Expose the port your application runs on
EXPOSE 8080
