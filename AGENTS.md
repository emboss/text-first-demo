# AGENTS.md - `todo-api`

## Project Context

This repository contains a Spring Boot REST API for managing Todos.

The API serves as a demo in a presentation about AI-assisted software development.
Favor simplicity, clarity, and ease of local setup over production-readiness or
advanced features.

## Required Reading

Treat the following as authoritative. Read them before any non-trivial change:

- `doc/api-endpoints.md` — HTTP API contract
- `doc/todo-class-diagram.md` — Todo resource model

## Mandatory Pre-Conditions

**Before modifying `doc/api-endpoints.md` or adding/changing any endpoint**, you
MUST read and follow `.agents/skills/create-endpoint/SKILL.md`. Do not edit the
contract or write any endpoint code until you have read that file.

## Tech Stack

- Java 25
- Spring Boot 4.0.6, Spring 7 — Spring MVC only, no reactive APIs
- `spring-boot-starter-webmvc` and `spring-boot-starter-webmvc-test`
- Gradle 9 with the Gradle Wrapper
- Bean Validation
- JUnit 6, Mockito, `@SpringBootTest` with `RestTestClient`
- In-memory storage via `Map<Long, Todo>`

## Runtime Conventions

- The application runs on port `8080`.
- Start with `./gradlew bootRun`.
- Run tests with `./gradlew test`.
- Keep the application self-contained and easy to run locally.

## Architecture

Shallow structure, close to standard Spring Boot conventions.
The initial boilerplate project structure was downloaded from
the Spring Initializr service.

Package responsibilities:

- `dev.martinbosslet.todoapi.todo` — Todo model, controller, service, Todo-specific exceptions
- `dev.martinbosslet.todoapi.web` — cross-cutting HTTP error handling (`ApiExceptionHandler`)
- `doc/` — authoritative API contract, model, and runnable curl examples

## Implementation Conventions

- Use constructor injection.
- Keep controllers focused on HTTP concerns; CRUD lives in `TodoService`.
- Store Todos in memory with `Map<Long, Todo>` owned by `TodoService`.
- Generate thread-safe Todo IDs on the server side.
- Do not add a database, repository layer, Docker setup, or authentication unless
  explicitly requested.
- Do not use Lombok.
- Prefer clear method names over clever abstractions.

## Todo Model Conventions

- The primary Todo DTO/model is named `Todo`.
- Fields and semantics are defined in `doc/todo-class-diagram.md`.
- Use `Boolean` instead of primitive `boolean` for optional boolean fields, so missing
  JSON fields do not automatically cause `400 Bad Request`.
- Validate request payloads at the HTTP boundary with Bean Validation.

## API Conventions

- Use JSON request and response bodies.
- Status codes:
  - `200 OK` for successful reads and updates
  - `201 Created` for newly created Todos
  - `204 No Content` for successful deletes
  - `400 Bad Request` for invalid requests
  - `404 Not Found` for unknown Todo IDs
- Map domain exceptions in `web/ApiExceptionHandler` — do not catch them in controllers.
- Keep response bodies direct and readable; do not add generic response wrappers.

## Testing Conventions

- Add or update tests alongside any behavior change.
- Use Mockito for controller unit tests.
- Use `@SpringBootTest` with `RestTestClient` for integration tests.
- Tests must stay in sync with `doc/api-endpoints.md`.

## Documentation

The following docs must stay in sync with the code:

- `README.md` — prerequisites, how to start the server, how to run tests, endpoint overview
- `doc/api-endpoints.md` — authoritative API contract
- `doc/curl-examples.md` — copyable `curl` commands for every endpoint, targeting `http://localhost:8080`
