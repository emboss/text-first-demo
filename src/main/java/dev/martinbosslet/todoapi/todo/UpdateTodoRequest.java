package dev.martinbosslet.todoapi.todo;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoRequest(
		@NotBlank String title,
		String description,
		Boolean completed) {
}
