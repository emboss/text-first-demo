---
name: create-endpoint
description: Add a new REST endpoint to the todo-api end-to-end (controller, service, tests, docs). Use when adding HTTP routes, expanding the API surface, or wiring a new operation through all layers.
---

# Create Endpoint Skill

This skill walks a new HTTP endpoint through every layer it has to touch:
contract, service, controller, tests, docs.

Global policy — tech stack, status codes, validation, error mapping — lives in
`AGENTS.md`. This skill does not repeat it. It covers the *procedure*: the
order of changes and the artifacts that must end up updated.

## Before you start

Read these once per session, before adding the first endpoint:

- `doc/api-endpoints.md` — current API contract (you will extend this)
- `doc/todo-class-diagram.md` — Todo fields and semantics
- `src/main/java/dev/demo/todo/web/ApiExceptionHandler.java` — error mapping

If a similar endpoint already exists, read it too and match its style.

## Workflow

1. **Update the contract first.** Add the new route to `doc/api-endpoints.md`:
   method, path, request shape, response shape, status codes, plus a sample
   request body and response body wherever the endpoint has them. The contract
   is authoritative; the code follows it.
2. **Service method.** Put the business logic on `TodoService`. Throw
   `TodoNotFoundException` (or a similar domain exception) for missing
   resources — do not return `null` or leak `Optional` to the controller.
3. **Controller method.** Map the HTTP route to the service. Annotate request
   body and path variables with Bean Validation. Return the status code
   prescribed by `AGENTS.md` ("API Conventions"). Do not catch domain
   exceptions here — `ApiExceptionHandler` translates them.
4. **Controller unit test (Mockito).** Mock `TodoService`. Cover at minimum:
   - happy path
   - validation failure (`400`) where the endpoint accepts a body or params
   - unknown id (`404`) where the endpoint takes a path variable
5. **Integration test (`RestTestClient`).** Hit the real route end-to-end.
   Assert status and body. One happy-path test plus one error-case test is
   the floor.
6. **curl example.** Append a copy-pasteable example to `doc/curl-examples.md`,
   targeting `http://localhost:8080`.
7. **README.** If the endpoint changes the API overview, update the endpoint
   list in `README.md`.

## Templates and examples

- `template.md` — skeletons for controller, service, unit test, integration
  test, and curl example
- `example.md` — fully worked example: `GET /todos/{id}`

Use them as starting points, not strict molds. Match the style of surrounding
code where it exists.

## Done criteria

- [ ] `doc/api-endpoints.md` reflects the new route
- [ ] Controller and service implemented per the contract
- [ ] Unit and integration tests pass: `./gradlew test`
- [ ] `doc/curl-examples.md` has a working example
- [ ] `README.md` endpoint list is current (if it lists endpoints individually)
