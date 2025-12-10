# Доска объявлений 


RESR API для платформы объявлений на Spring Boot.

### Установка

1. Клонировать репозиторий
2. Настроить БД в application.properties
3. Запустить приложение 
```bash
mvn spring-boot:run
```

## Документация API
После запуска доступно
- Swagger UI
- OpenAPI spec

## Аутентификация 
Используется Basic Auth. Для доступа к защищенным эндпоинтам:

```text
Authorization: Basic base64(email:password)
```
Публичные эндпоинты:
- POST /register - регистрация

- POST /login - вход

- GET /ads - просмотр объявлений

- GET /images/** - получение изображений

## Архитектура
```text
Controller → Service → Repository → Database
    ↑           ↑           ↑
   HTTP     Бизнес-логика   JPA
   DTO        Мапперы      Entity
```

## Основные сущности

- Пользователь - может создавать объявления и комментарии

- Объявление - содержит заголовок, описание, цену, изображение

- Комментарий - отзывы к объявлениям

- Изображение - хранится в БД как binary data

## Структура проекта

```text
src/main/java/ru/skypro/homework/
├── config/         # Конфигурация Spring Security
├── controller/     # REST контроллеры
├── dto/            # Data Transfer Objects
├── entity/         # JPA сущности (таблицы БД)
├── mapper/         # Преобразователи Entity ↔ DTO
├── repository/     # Spring Data JPA репозитории
├── service/        # Бизнес-логика
└── filter/         # HTTP фильтры (CORS)
```

## Технологии
- Spring Boot 2.7 - основной фреймворк

- Spring Security - аутентификация и авторизация

- Spring Data JPA - работа с базой данных

- PostgreSQL - реляционная БД

- Lombok - уменьшение boilerplate-кода

- Swagger - документация API

## Основные функции

- Регистрация и аутентификация пользователей
-  CRUD операции с объявлениями
-  Загрузка и отображение изображений
-  Комментирование объявлений
-  Управление профилем пользователя
-  Ролевая модель (USER/ADMIN)
-  Проверка прав доступа

## Конфигурация

- Основные настройки в application.properties:

- Порт сервера: server.port=8080

- Подключение к PostgreSQL

- Размер загружаемых файлов: 10MB

- Автосоздание таблиц: spring.jpa.hibernate.ddl-auto=update


























