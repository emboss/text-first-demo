# Endpoint Templates

Starting points for a new endpoint. Adapt route, types, and assertions to the
endpoint you are actually adding. These snippets exist so you do not start
from a blank file — they are not a strict mold.

## Service method

```java
public Todo create(TodoCreateRequest request) {
    long id = nextId.incrementAndGet();
    Todo todo = new Todo(id, request.title(), request.description(), Boolean.FALSE);
    todos.put(id, todo);
    return todo;
}
```

For lookups that may miss, throw a domain exception — do not return `null`:

```java
public Todo findById(long id) {
    Todo todo = todos.get(id);
    if (todo == null) {
        throw new TodoNotFoundException(id);
    }
    return todo;
}
```

## Controller method

```java
@PostMapping("/todos")
public ResponseEntity<Todo> create(@Valid @RequestBody TodoCreateRequest request) {
    Todo created = todoService.create(request);
    URI location = URI.create("/todos/" + created.id());
    return ResponseEntity.created(location).body(created);
}
```

The controller does not catch `TodoNotFoundException` — `ApiExceptionHandler`
translates it to `404`.

## Controller unit test (Mockito)

```java
@Test
void createReturns201WithBody() throws Exception {
    Todo created = new Todo(1L, "buy milk", null, false);
    when(todoService.create(any())).thenReturn(created);

    mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "buy milk"}
                """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/todos/1"))
        .andExpect(jsonPath("$.id").value(1));
}

@Test
void createReturns400WhenTitleMissing() throws Exception {
    mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());
}
```

## Integration test (`RestTestClient`)

```java
@Test
void createPersistsTodo() {
    restTestClient.post().uri("/todos")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Map.of("title", "buy milk"))
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.title").isEqualTo("buy milk");
}
```

## curl example

```bash
curl -i -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"buy milk"}'
```
