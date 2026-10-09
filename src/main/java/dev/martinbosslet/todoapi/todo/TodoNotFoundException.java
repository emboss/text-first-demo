package dev.martinbosslet.todoapi.todo;

public class TodoNotFoundException extends RuntimeException {

	public TodoNotFoundException(long id) {
		super("Todo not found: " + id);
	}

}
