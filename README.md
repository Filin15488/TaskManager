# Task Manager

## ✨ Описание
**Task Manager** — это backend-приложение для управления задачами, разработанное на Java с использованием Spring Boot. Оно предоставляет API для работы с задачами, тегами, пользователями и категориями.

## 🚀 Функционал
- 📌 **API для создания задач** — возможность добавлять задачи через REST API.
- ✅ **Обновление и удаление задач** — изменение статуса и редактирование задач.
- 📅 **Дедлайны** — устанавливайте сроки выполнения задач.
- 🏷 **Категории и теги** — группируйте задачи по категориям и тегам.
- 🔧 **Простой запуск** — использование Docker позволяет быстро развернуть приложение

## 🛠 Технологии
- **Backend:** Java 21, Spring Boot
- **Database:** PostgreSQL
- **API:** REST, Swagger
- **Аутентификация:** JWT
- **Контейнеризация:** Docker

## 📦 Установка и запуск

### 1. Клонирование репозитория
```sh
git https://github.com/Filin15488/TaskManager.git
cd ./TaskManager
```

### 2. Запуск и сборка
```sh
docker compose up -d
```

Приложение готово к работе! Оно будет доступно по адресу: `http://localhost:8080`

### 3.  Альтернативный запуск вручную
#### Установка зависимостей
```shell
mvn install
```
#### Конфигурация базы данных
Создайте базу данных PostgreSQL и укажите параметры подключения в файле `application.properties`:
```java
spring.datasource.url=jdbc:postgresql://localhost:5432/TaskManager
spring.datasource.username=your_username
spring.datasource.password=your_password
```
#### Запуск приложения
```shell
mvn spring-boot:run
```

## 📖 Документация API
После запуска приложения документация API будет доступна по адресу: `http://localhost:8080/swagger-ui.html`

Помимо этого примеры запросов представлены в коллекции postman в директории `TaskManager/postman`
## 💼 Администратор

По умолчанию создаётся пользователь с ролью "Администратор". Логин и пароль "admin"

Preview:
```
POST /api/auth/login HTTP/1.1
Content-Length: 49
Content-Type: application/json
Host: localhost:8080
{
  "username": "admin",
  "password": "admin"
}
```
