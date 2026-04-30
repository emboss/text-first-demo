# Initial TODO API Implementation Plan

## Summary
Implement the CRUD API already defined in `doc/api-endpoints.md`: list, get, create, update, and delete Todos. Keep the app simple: Spring MVC controller, in-memory service, direct JSON bodies, Bean Validation at the HTTP boundary, and no persistence layer beyond `Map<Long, Todo>`.

Baseline check: current `./gradlew test` passes.

## Implementation Steps
1. **Confirm and align docs first**
   - Treat `doc/api-endpoints.md` and `doc/todo-class-diagram.md` as the contract.
   - Add `doc/curl-examples.md`, since it is required but currently missing.
   - Expand `README.md` with prerequisites, start/test commands, and the endpoint overview.

2. **Add the Todo domain/API model**
   - Create package `dev.martinbosslet.todoapi.todo`.
   - Add `Todo` as the response/model type with `Long id`, `String title`, `String description`, `Boolean completed`.
   - Add `CreateTodoRequest` and `UpdateTodoRequest`.
   - Validate `title` with Bean Validation, at minimum `@NotBlank`.
   - Keep `description` and `completed` optional.
   - Default missing `completed` to `false` on both create and update.

3. **Add service behavior**
   - Add `TodoService` with an in-memory `Map<Long, Todo>` and thread-safe ID generation.
   - Implement `findAll`, `findById`, `create`, `update`, and `delete`.
   - Throw `TodoNotFoundException` for missing IDs.
   - Do not leak `Optional`, `null`, or HTTP concerns out of the service.

4. **Add HTTP and error handling**
   - Add `TodoController` with:
     - `GET /todos`
     - `GET /todos/{id}`
     - `POST /todos`
     - `PUT /todos/{id}`
     - `DELETE /todos/{id}`
   - Return `201 Created` with `Location: /todos/{id}` for creates.
   - Return `204 No Content` for deletes.
   - Add `dev.martinbosslet.todoapi.web.ApiExceptionHandler` to map `TodoNotFoundException` to `404 Not Found`.
   - Let validation failures produce `400 Bad Request`.

5. **Add tests**
   - Add `TodoControllerTest` using `@WebMvcTest` and Mockito.
   - Cover happy paths for all endpoints.
   - Cover validation failures for create/update.
   - Cover `404` behavior for get/update/delete unknown IDs.
   - Add `TodoApiIntegrationTest` using `@SpringBootTest` and `RestTestClient`.
   - Cover end-to-end create, list, get, update, delete, plus at least one validation error and one missing-ID error.

6. **Verify**
   - Run `./gradlew test`.
   - Ensure tests, `README.md`, `doc/api-endpoints.md`, and `doc/curl-examples.md` describe the same API behavior.

## Public API
- `GET /todos` returns `200 OK` with a JSON array of Todos.
- `GET /todos/{id}` returns `200 OK` with one Todo or `404 Not Found`.
- `POST /todos` accepts create JSON and returns `201 Created`.
- `PUT /todos/{id}` accepts update JSON and returns `200 OK` or `404 Not Found`.
- `DELETE /todos/{id}` returns `204 No Content` or `404 Not Found`.

## Assumptions
- Implement exactly the CRUD surface already listed in `doc/api-endpoints.md`.
- No database, repository layer, Docker, auth, pagination, filtering, or PATCH endpoint.
- Missing `completed` means `false` for create and update.
- Error response bodies may use Spring’s standard readable error representation; tests should assert required status codes and only assert body details where the contract specifies them.
