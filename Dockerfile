# ===== STAGE 1: Сборка приложения =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости отдельно (для кэширования)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем остальной код и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests

# ===== STAGE 2: Запуск приложения =====
FROM eclipse-temurin:17-jdk-alpine

# Создаем директорию для приложения
WORKDIR /app

# Копируем jar-файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем порт (Spring Boot по умолчанию 8080)
EXPOSE 8080

# Устанавливаем переменные окружения (можно переопределять через docker-compose)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgr \
    SPRING_DATASOURCE_USERNAME=user \
    SPRING_DATASOURCE_PASSWORD=password \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]
