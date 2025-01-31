# Стадия 1: Сборка JAR-файла
FROM maven:3.9.9-eclipse-temurin-21 AS builder
# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app
# Копируем файлы проекта
COPY pom.xml .
COPY src ./src
# Проверяем наличие файлов в target и запускаем сборку, если папка пустая
RUN test -d target && echo "Target folder exists, skipping Maven build" || mvn clean package -DskipTests


# Стадия 2: Минимальный образ для запуска приложения
FROM openjdk:21-jdk-slim
# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app
# Копируем JAR-файл из предыдущей стадии
#COPY --from=builder /app/target/*.jar /opt/app/*.jar
COPY --from=builder /app/target/taskManager.jar app.jar
# Указываем порт для приложения
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
