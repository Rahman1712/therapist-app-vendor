FROM openjdk:17-alpine
add target/therapist-service.jar therapist-service.jar
ENTRYPOINT [ "java", "-jar", "therapist-service.jar" ]
