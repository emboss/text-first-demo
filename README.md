# Todo REST API

This repository contains a Spring Boot REST API for managing Todos.

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
| `GET` | `/todos` | List all Todos |
| `GET` | `/todos/{id}` | Get one Todo by ID |
| `POST` | `/todos` | Create a Todo |
| `PUT` | `/todos/{id}` | Replace/update a Todo |
| `DELETE` | `/todos/{id}` | Delete a Todo |

Todos are stored in memory, so data resets when the application restarts.

For the full API contract, see `doc/api-endpoints.md`. For copyable examples,
see `doc/curl-examples.md`.
