package dev.martinbosslet.todoapi.todo;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodoController {

	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public List<Todo> findAll() {
		return todoService.findAll();
	}

	@GetMapping("/{id}")
	public Todo findById(@PathVariable long id) {
		return todoService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Todo> create(@Valid @RequestBody CreateTodoRequest request) {
		Todo created = todoService.create(request);
		return ResponseEntity.created(URI.create("/todos/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public Todo update(@PathVariable long id, @Valid @RequestBody UpdateTodoRequest request) {
		return todoService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		todoService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
