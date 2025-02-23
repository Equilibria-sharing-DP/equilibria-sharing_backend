# Backend-Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/equilibria-sharing-0.0.1-SNAPSHOT.jar /app/backend.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/backend.jar"]
