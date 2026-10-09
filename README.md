# ToDo REST API

This repository contains a Spring Boot REST API for managing ToDos.

## Prerequisites

- Java 25
- Gradle Wrapper included in this repository

## Run the API

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080`.

## Run Tests

```bash
./gradlew test
```

## Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/todos` | List all ToDos |
| `GET` | `/todos/{id}` | Get one ToDo by ID |
| `POST` | `/todos` | Create a ToDo |
| `PUT` | `/todos/{id}` | Replace/update a ToDo |
| `DELETE` | `/todos/{id}` | Delete a ToDo |

ToDos are stored in memory, so data resets when the application restarts.

For the full API contract, see `doc/api-endpoints.md`. For copyable examples,
see `doc/curl-examples.md`.
