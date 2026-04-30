package dev.martinbosslet.todoapi.todo;

public record Todo(Long id, String title, String description, Boolean completed) {
}
