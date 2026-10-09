package dev.martinbosslet.todoapi.todo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExampleIntegrationTest {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void createThenGetTodo() {
		Todo created = restTestClient.post().uri("/todos")
			.contentType(MediaType.APPLICATION_JSON)
			.body(new CreateTodoRequest("Buy milk", null, null))
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().valueEquals("Location", "/todos/1")
			.returnResult(Todo.class)
			.getResponseBody();

		assertThat(created).isNotNull();
		assertThat(created.completed()).isFalse();

		restTestClient.get().uri("/todos/{id}", created.id())
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(created.id().intValue())
			.jsonPath("$.title").isEqualTo("Buy milk")
			.jsonPath("$.completed").isEqualTo(false);
	}

	@Test
	void unknownTodoReturns404() {
		restTestClient.get().uri("/todos/999")
			.exchange()
			.expectStatus().isNotFound();
	}
}
