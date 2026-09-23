# Call Center System

Учебная система телефонного колл-центра.

Проект состоит из нескольких сервисов:

- `phone` — веб-приложение телефонии на GWT + Java Servlet;
- `ats` — сервис АТС на Spring Boot;
- `postgres` — база данных PostgreSQL;
- `swagger-ui` — документация REST API;
- `openapi-render` — генерация OpenAPI-спецификации с параметрами из `.env`.

## Требования

Для запуска проекта необходимы:

- Docker;
- Docker Compose.

Для локальной сборки `phone` без Docker дополнительно необходимы:

- Java 8;
- Apache Ant;
- GWT 2.7.0.

## Настройка окружения

В корне проекта находится файл:

```text
.env.example
```

Перед запуском необходимо создать на его основе `.env`.

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux/macOS

```bash
cp .env.example .env
```

После этого при необходимости измените значения переменных в `.env`.

Пример:

```env
PUBLIC_HOST=localhost

ATS_PORT=8080
PHONE_PORT=8081
SWAGGER_PORT=8082

DB_NAME=gwt_devices
DB_USERNAME=postgres
DB_PASSWORD=change-me

INTERNAL_TOKEN=change-me
```

## Запуск через Docker Compose

Собрать и запустить все сервисы:

```bash
docker compose up -d --build
```

Проверить состояние контейнеров:

```bash
docker compose ps
```

Посмотреть общие логи:

```bash
docker compose logs -f
```

Логи сервиса телефонии:

```bash
docker compose logs -f phone
```

Логи сервиса АТС:

```bash
docker compose logs -f ats
```

## Доступ к сервисам

Адреса зависят от значений `PUBLIC_HOST`, `PHONE_PORT`, `ATS_PORT` и `SWAGGER_PORT` в `.env`.

При стандартном примере конфигурации:

- Телефония: `http://localhost:8081`
- АТС: `http://localhost:8080`
- Swagger UI: `http://localhost:8082`

## Остановка проекта

Остановить контейнеры:

```bash
docker compose down
```

Остановить контейнеры и удалить volumes:

```bash
docker compose down -v
```

> При использовании `-v` будут удалены данные PostgreSQL, хранящиеся в Docker volume.

## REST API

Основные endpoint'ы сервиса телефонии:

| Метод | Endpoint | Назначение |
|---|---|---|
| `POST` | `/api/queue` | Добавить входящий звонок в очередь |
| `DELETE` | `/api/queue` | Отменить входящий звонок |
| `GET` | `/api/queue` | Получить очередь входящих звонков |
| `POST` | `/api/calls` | Ответить на входящий звонок |
| `DELETE` | `/api/calls` | Завершить активный звонок |
| `GET` | `/api/calls` | Получить список активных звонков |
| `GET` | `/api/rooms` | Получить список комнат |
| `GET` | `/api/rooms/devices` | Получить аппараты выбранной комнаты |

Полное описание API доступно через Swagger UI.

## Конфигурация приложения

Runtime-конфигурация приложения передаётся через переменные окружения Docker Compose и используется через `application.properties`.

Основные переменные:

```text
ATS_URL
INTERNAL_TOKEN
DB_URL
DB_USERNAME
DB_PASSWORD
ATS_ALLOWED_ORIGIN
PHONE_ALLOWED_ORIGIN
SWAGGER_ALLOWED_ORIGIN
```

Обязательная конфигурация проверяется при старте web-приложения. Если необходимая переменная окружения отсутствует, приложение не должно успешно разворачиваться.

## Локальная сборка phone

Для локальной сборки сервиса `phone` необходимо создать файл:

```text
phone/local.properties
```

на основе:

```text
phone/local.properties.example
```

Пример для Windows:

```properties
gwt.sdk=C:/gwt/gwt-2.7.0
gwt.libs=C:/java/libs
gwt.java=C:/Program Files/Java/jdk1.8.0_241/bin/java.exe
```

Файл `local.properties` содержит пути конкретной рабочей машины и не должен добавляться в Git.

После настройки перейдите в каталог `phone`:

```bash
cd phone
```

Сборка приложения:

```bash
ant build
```

Сборка WAR:

```bash
ant war
```

Очистка результатов сборки:

```bash
ant clean
```

При Docker-сборке локальные пути не используются: необходимые значения передаются в Ant через параметры `-D`.

## OpenAPI

Спецификация OpenAPI хранится в шаблоне:

```text
phone/openapi.template.yaml
```

При запуске Docker Compose сервис `openapi-render` подставляет в шаблон значения `PUBLIC_HOST` и `PHONE_PORT` и создаёт итоговый `openapi.yaml` для Swagger UI.

Это позволяет не хранить адрес сервера телефонии захардкоженным в спецификации.

## Структура проекта

```text
.
├── ats/                         # сервис АТС
├── phone/                       # сервис телефонии и GWT-клиент
│   ├── src/
│   ├── war/
│   ├── build.xml
│   ├── local.properties.example
│   └── openapi.template.yaml
├── docker-compose.yml
├── .env.example
└── README.md
```

## Первый запуск

Минимальная последовательность действий:

```bash
git clone <repository-url>
cd <repository-directory>
```

Создать `.env`.

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux/macOS

```bash
cp .env.example .env
```

Запустить проект:

```bash
docker compose up -d --build
```

Проверить контейнеры:

```bash
docker compose ps
```

После запуска открыть Swagger UI по адресу:

```text
http://<PUBLIC_HOST>:<SWAGGER_PORT>
```

Например:

```text
http://localhost:8082
```
