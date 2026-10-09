package dev.martinbosslet.todoapi.todo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class TodoService {

	private final AtomicLong nextId = new AtomicLong();

	private final Map<Long, Todo> todos = new ConcurrentHashMap<>();

	public List<Todo> findAll() {
		return todos.values().stream()
				.sorted(Comparator.comparing(Todo::id))
				.toList();
	}

	public Todo findById(long id) {
		Todo todo = todos.get(id);
		if (todo == null) {
			throw new TodoNotFoundException(id);
		}
		return todo;
	}

	public Todo create(CreateTodoRequest request) {
		long id = nextId.incrementAndGet();
		Todo todo = new Todo(id, request.title(), request.description(), completedOrFalse(request.completed()));
		todos.put(id, todo);
		return todo;
	}

	public Todo update(long id, UpdateTodoRequest request) {
		if (!todos.containsKey(id)) {
			throw new TodoNotFoundException(id);
		}
		Todo todo = new Todo(id, request.title(), request.description(), completedOrFalse(request.completed()));
		todos.put(id, todo);
		return todo;
	}

	public void delete(long id) {
		Todo removed = todos.remove(id);
		if (removed == null) {
			throw new TodoNotFoundException(id);
		}
	}

	private Boolean completedOrFalse(Boolean completed) {
		return Boolean.TRUE.equals(completed);
	}

}
