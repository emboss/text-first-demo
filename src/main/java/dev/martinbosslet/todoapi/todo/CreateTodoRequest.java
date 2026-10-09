package dev.martinbosslet.todoapi.todo;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoRequest(
		@NotBlank String title,
		String description,
		Boolean completed) {
}
