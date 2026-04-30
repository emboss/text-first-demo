package dev.martinbosslet.todoapi.todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
class ExampleWebMcvTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService todoService;

	@Test
	void createTodoReturns201WithLocationAndBody() throws Exception {
		when(todoService.create(any()))
			.thenReturn(new Todo(1L, "Buy milk", null, false));

		mockMvc.perform(post("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "title": "Buy milk"
						}
						"""))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/todos/1"))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.title").value("Buy milk"))
			.andExpect(jsonPath("$.completed").value(false));
	}

	@Test
	void createTodoReturns400WhenTitleIsBlank() throws Exception {
		mockMvc.perform(post("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "title": "   "
						}
						"""))
			.andExpect(status().isBadRequest());
	}
}
