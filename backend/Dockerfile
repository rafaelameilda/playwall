FROM maven:3.9.8-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src

EXPOSE 8090

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]