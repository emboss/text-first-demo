# ToDo API curl Examples

These examples target a locally running API at `http://localhost:8080`.

## List Todos

```bash
curl -i http://localhost:8080/todos
```

## Get Todo

```bash
curl -i http://localhost:8080/todos/1
```

## Create Todo

```bash
curl -i -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy milk","description":"Pick up milk after work","completed":false}'
```

## Update Todo

```bash
curl -i -X PUT http://localhost:8080/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy oat milk","description":"Pick up oat milk after work","completed":true}'
```

## Delete Todo

```bash
curl -i -X DELETE http://localhost:8080/todos/1
```
