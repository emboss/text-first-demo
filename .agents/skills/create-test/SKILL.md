---
name: create-test
description: Add or update tests for the Todo Spring Boot 4 REST API, especially @WebMvcTest controller slice tests and RestTestClient integration tests.
---

# Create Test Skill

Use this skill when adding or updating tests in this project.

## Test Style

- Keep tests small and readable; this is a teaching project, not a production test pyramid.
- Match the API contract in `doc/api-endpoints.md`.
- Use JUnit 6, AssertJ where direct object assertions help, and Mockito for mocked collaborators.
- Prefer testing observable HTTP behavior: status codes, headers, and JSON body fields.
- Cover one happy path and the important error paths for each behavior changed.

## Spring Boot 4 Conventions

Use these imports and APIs:

- `@WebMvcTest`: `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
- `@AutoConfigureMockMvc`: `org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc`
- `@AutoConfigureRestTestClient`: `org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient`
- `RestTestClient`: `org.springframework.test.web.servlet.client.RestTestClient`
- `@MockitoBean`: `org.springframework.test.context.bean.override.mockito.MockitoBean`

Do not copy older Spring examples that use `@MockBean` or WebFlux-style
`WebTestClient.bodyValue(...)`. In this project, `RestTestClient` request bodies
use `.body(...)`.

## Controller Tests

- Use `@WebMvcTest(TodoController.class)` for controller slice tests.
- Inject `MockMvc`.
- Mock `TodoService` with `@MockitoBean`.
- Verify:
  - success status and JSON body
  - `201 Created` and `Location` for create endpoints
  - `400 Bad Request` for validation failures
  - `404 Not Found` for domain not-found exceptions
- Keep CRUD logic out of controller tests; the service is mocked.

See `ExampleWebMcvTest.java` for a compact controller test.

## Integration Tests

- Use `@SpringBootTest` plus `@AutoConfigureRestTestClient`.
- Inject `RestTestClient`.
- Exercise real routes end-to-end through the Spring context.
- Use `@DirtiesContext` when tests mutate the in-memory `TodoService` and depend
  on clean IDs or empty storage.
- Verify persistence through a second request when useful, such as create then get
  or delete then get.

See `ExampleIntegrationTest.java` for a compact integration test.
