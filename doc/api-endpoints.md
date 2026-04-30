# ToDo API Endpoints

This API exposes a simple REST interface for managing ToDo resources.

## Resource Model

```json
{
  "id": 1,
  "title": "Buy milk",
  "description": "Pick up milk after work",
  "completed": false
}
```

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Server-generated Todo identifier. |
| `title` | `String` | Required human-readable title. |
| `description` | `String` | Optional longer description. |
| `completed` | `Boolean` | Optional completion flag. Defaults to `false` on create and update. |

## Endpoints

| Method | Path | Purpose | Success |
| --- | --- | --- | --- |
| `GET` | `/todos` | List all Todos. | `200 OK` |
| `GET` | `/todos/{id}` | Get one Todo by ID. | `200 OK` |
| `POST` | `/todos` | Create a Todo. | `201 Created` |
| `PUT` | `/todos/{id}` | Replace/update a Todo. | `200 OK` |
| `DELETE` | `/todos/{id}` | Delete a Todo. | `204 No Content` |

## List Todos

`GET /todos`

Returns all Todos.

### Response

Status: `200 OK`

```json
[
  {
    "id": 1,
    "title": "Buy milk",
    "description": "Pick up milk after work",
    "completed": false
  }
]
```

## Get Todo

`GET /todos/{id}`

Returns one Todo by ID.

### Response

Status: `200 OK`

```json
{
  "id": 1,
  "title": "Buy milk",
  "description": "Pick up milk after work",
  "completed": false
}
```

### Errors

| Status | Reason |
| --- | --- |
| `404 Not Found` | No Todo exists with the requested ID. |

## Create Todo

`POST /todos`

Creates a new Todo. The server generates the `id`.
If `completed` is omitted, it defaults to `false`.

### Request

```json
{
  "title": "Buy milk",
  "description": "Pick up milk after work",
  "completed": false
}
```

### Response

Status: `201 Created`

```json
{
  "id": 1,
  "title": "Buy milk",
  "description": "Pick up milk after work",
  "completed": false
}
```

### Errors

| Status | Reason |
| --- | --- |
| `400 Bad Request` | The request body is invalid. |

## Update Todo

`PUT /todos/{id}`

Replaces or updates an existing Todo.
If `completed` is omitted, it defaults to `false`.

### Request

```json
{
  "title": "Buy oat milk",
  "description": "Pick up oat milk after work",
  "completed": true
}
```

### Response

Status: `200 OK`

```json
{
  "id": 1,
  "title": "Buy oat milk",
  "description": "Pick up oat milk after work",
  "completed": true
}
```

### Errors

| Status | Reason |
| --- | --- |
| `400 Bad Request` | The request body is invalid. |
| `404 Not Found` | No Todo exists with the requested ID. |

## Delete Todo

`DELETE /todos/{id}`

Deletes an existing Todo.

### Response

Status: `204 No Content`

### Errors

| Status | Reason |
| --- | --- |
| `404 Not Found` | No Todo exists with the requested ID. |
