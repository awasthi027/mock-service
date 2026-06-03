FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/mock-service-0.0.1-SNAPSHOT.jar app.jar

ENV DATABASE_URL=jdbc:postgresql://localhost:5432/mockservice
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=password
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

