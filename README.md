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

Задайте в `.env` собственные значения `DB_PASSWORD` и `INTERNAL_TOKEN`. При необходимости измените адрес сервера и порты. Значения `change-me` ниже — примеры для заполнения.

Файл `.env` содержит пароль БД и токен и не должен добавляться в Git.

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

## Проверка API через Postman

Коллекция запросов находится в корне проекта: `postman_collection.json`.

### Импорт коллекции

Откройте Postman, нажмите **Import** и выберите `postman_collection.json`.

При стандартных портах из `.env.example` запросы к АТС отправляются на `http://127.0.0.1:8080`, а к телефонии — на `http://127.0.0.1:8081`. Если сервисы запущены на другом компьютере или порты изменены, поправьте адреса запросов.

### Создание и выбор окружения Local

1. Откройте раздел **Environments** в Postman.
2. Создайте окружение с именем **Local**.
3. Добавьте переменную `INTERNAL_TOKEN`. В её локальное значение вставьте токен из `.env`, с которым запущена АТС. Вставлять нужно только значение, без префикса `INTERNAL_TOKEN=`.
4. В списке окружений **справа сверху** выберите **Local**. Вместо **No environment** или имени другого окружения должно отображаться **Local**.

| Переменная | Значение |
|---|---|
| `INTERNAL_TOKEN` | Значение `INTERNAL_TOKEN` из действующего `.env` |

`Local` — название набора переменных в Postman. При отправке запроса Postman использует переменные активного окружения.

Postman не читает `.env` автоматически: значение нужно перенести вручную. `.env.example` служит только образцом; токен должен совпадать со значением у запущенной АТС.

### Заголовок запроса к АТС

Откройте запрос **«Отправить событие»** (`POST /api/action`) и во вкладке **Headers** проверьте заголовки:

| Key | Value |
|---|---|
| `X-Internal-Token` | `{{INTERNAL_TOKEN}}` |
| `Content-Type` | `application/json` |

Если в `X-Internal-Token` записан конкретный токен, замените его на `{{INTERNAL_TOKEN}}` и сохраните запрос. Тело запроса отправляется как **Body → raw → JSON**.

При отправке Postman заменит `{{INTERNAL_TOKEN}}` значением из окружения `Local`. Чтобы проверить подстановку, наведите указатель на переменную в заголовке.

Токен передаётся в заголовке `X-Internal-Token`; добавлять его в параметры URL не нужно.

Если АТС возвращает `403 Forbidden`, проверьте, что выбрано окружение `Local` и значение его переменной совпадает с токеном запущенной АТС.

Если вы изменили `INTERNAL_TOKEN` в `.env` после запуска проекта, примените новое значение к обоим сервисам:

```bash
docker compose up -d ats phone
```

Затем обновите значение `INTERNAL_TOKEN` в Postman. Команда Compose пересоздаёт контейнеры, конфигурация которых изменилась.

После изменения заголовка сохраните запрос и экспортируйте коллекцию в `postman_collection.json`, заменив файл в проекте. В коллекции должен остаться `{{INTERNAL_TOKEN}}`. Реальный токен задаётся в локальном значении переменной окружения; файл окружения с токеном не добавляется в репозиторий.

Подробнее: [окружения Postman](https://learning.postman.com/docs/use/send-requests/variables/managing-environments) и [применение конфигурации Docker Compose](https://docs.docker.com/reference/cli/docker/compose/up/).

## Конфигурация приложения

Конфигурация сервиса `phone` передаётся через переменные окружения Docker Compose и считывается через `application.properties`.

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
├── postman_collection.json      # коллекция запросов Postman
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

