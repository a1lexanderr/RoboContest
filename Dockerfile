FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgr \
    SPRING_DATASOURCE_USERNAME=user \
    SPRING_DATASOURCE_PASSWORD=password \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update

ENTRYPOINT ["java", "-jar", "app.jar"]
