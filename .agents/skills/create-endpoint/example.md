# Worked Example: `GET /todos/{id}`

A complete walk-through of the workflow in `SKILL.md`, applied to a single
endpoint that fetches one Todo by id.

## 1. Contract entry — `doc/api-endpoints.md`

````markdown
### GET /todos/{id}

Fetch a single Todo by id.

**Path parameters**
- `id` (long, required) — Todo id

**Responses**

`200 OK` — Todo as JSON:

```json
{
  "id": 1,
  "title": "buy milk",
  "description": null,
  "completed": false
}
```

`404 Not Found` — no Todo with that id
````

## 2. Service method — `TodoService.java`

```java
public Todo findById(long id) {
    Todo todo = todos.get(id);
    if (todo == null) {
        throw new TodoNotFoundException(id);
    }
    return todo;
}
```

## 3. Controller method — `TodoController.java`

```java
@GetMapping("/todos/{id}")
public Todo findById(@PathVariable long id) {
    return todoService.findById(id);
}
```

`TodoNotFoundException` is mapped to `404` by `ApiExceptionHandler`. The
controller stays free of error-handling noise.

## 4. Controller unit test — `TodoControllerTest.java`

```java
@Test
void findByIdReturnsTodo() throws Exception {
    when(todoService.findById(1L))
        .thenReturn(new Todo(1L, "buy milk", null, false));

    mockMvc.perform(get("/todos/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("buy milk"));
}

@Test
void findByIdReturns404ForUnknownId() throws Exception {
    when(todoService.findById(99L))
        .thenThrow(new TodoNotFoundException(99L));

    mockMvc.perform(get("/todos/99"))
        .andExpect(status().isNotFound());
}
```

## 5. Integration test — `TodoApiIntegrationTest.java`

```java
@Test
void getReturnsExistingTodo() {
    restTestClient.get().uri("/todos/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.title").isNotEmpty();
}

@Test
void getReturns404ForUnknownId() {
    restTestClient.get().uri("/todos/999999")
        .exchange()
        .expectStatus().isNotFound();
}
```

## 6. curl example — `doc/curl-examples.md`

```bash
# Fetch a Todo by id
curl -i http://localhost:8080/todos/1
```

## 7. README endpoint overview

```markdown
- `GET /todos/{id}` — fetch a single Todo
```

## What changed, in one diff-sized summary

- 1 contract entry
- 1 service method
- 1 controller method
- 2 unit tests, 2 integration tests
- 1 curl example
- 1 README line

No new classes, no new packages, no error-handling code in the controller —
the existing `ApiExceptionHandler` already covers `404`.
