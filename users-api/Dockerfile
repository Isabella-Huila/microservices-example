# Users API Dockerfile with Maven build
FROM maven:3.6.3-jdk-8 as builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/users-api-0.0.1-SNAPSHOT.jar /app/users-api.jar

ENV JWT_SECRET=PRFT
ENV SERVER_PORT=8083

EXPOSE 8083

CMD ["java", "-jar", "users-api.jar"]