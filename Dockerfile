FROM openjdk:11
COPY target/booking-management-system-1.0.jar booking-app.jar
ENTRYPOINT ["java", "-jar", "booking-app.jar"]