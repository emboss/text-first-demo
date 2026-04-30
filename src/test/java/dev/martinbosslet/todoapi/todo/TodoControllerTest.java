package dev.martinbosslet.todoapi.todo;

import java.util.List;

import dev.martinbosslet.todoapi.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import(ApiExceptionHandler.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService todoService;

	@Test
	void findAllReturnsTodos() throws Exception {
		when(todoService.findAll()).thenReturn(List.of(new Todo(1L, "Buy milk", "Pick up milk", false)));

		mockMvc.perform(get("/todos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].title").value("Buy milk"))
				.andExpect(jsonPath("$[0].description").value("Pick up milk"))
				.andExpect(jsonPath("$[0].completed").value(false));
	}

	@Test
	void findByIdReturnsTodo() throws Exception {
		when(todoService.findById(1L)).thenReturn(new Todo(1L, "Buy milk", null, false));

		mockMvc.perform(get("/todos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Buy milk"))
				.andExpect(jsonPath("$.completed").value(false));
	}

	@Test
	void findByIdReturns404ForUnknownId() throws Exception {
		when(todoService.findById(99L)).thenThrow(new TodoNotFoundException(99L));

		mockMvc.perform(get("/todos/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createReturns201WithLocationAndBody() throws Exception {
		when(todoService.create(any(CreateTodoRequest.class)))
				.thenReturn(new Todo(1L, "Buy milk", "Pick up milk", false));

		mockMvc.perform(post("/todos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "title": "Buy milk",
								  "description": "Pick up milk"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/todos/1"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Buy milk"))
				.andExpect(jsonPath("$.description").value("Pick up milk"))
				.andExpect(jsonPath("$.completed").value(false));
	}

	@Test
	void createReturns400WhenTitleMissing() throws Exception {
		mockMvc.perform(post("/todos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "description": "Pick up milk"
								}
								"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateReturnsTodo() throws Exception {
		when(todoService.update(anyLong(), any(UpdateTodoRequest.class)))
				.thenReturn(new Todo(1L, "Buy oat milk", "Pick up oat milk", true));

		mockMvc.perform(put("/todos/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "title": "Buy oat milk",
								  "description": "Pick up oat milk",
								  "completed": true
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Buy oat milk"))
				.andExpect(jsonPath("$.description").value("Pick up oat milk"))
				.andExpect(jsonPath("$.completed").value(true));
	}

	@Test
	void updateReturns400WhenTitleMissing() throws Exception {
		mockMvc.perform(put("/todos/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "completed": true
								}
								"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateReturns404ForUnknownId() throws Exception {
		when(todoService.update(anyLong(), any(UpdateTodoRequest.class)))
				.thenThrow(new TodoNotFoundException(99L));

		mockMvc.perform(put("/todos/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "title": "Buy oat milk"
								}
								"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteReturns204() throws Exception {
		mockMvc.perform(delete("/todos/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteReturns404ForUnknownId() throws Exception {
		doThrow(new TodoNotFoundException(99L)).when(todoService).delete(99L);

		mockMvc.perform(delete("/todos/99"))
				.andExpect(status().isNotFound());
	}

}
