package dev.martinbosslet.todoapi;

import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
class TodoApiIntegrationTest {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void crudFlowCreatesListsGetsUpdatesAndDeletesTodo() {
		restTestClient.post().uri("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of(
						"title", "Buy milk",
						"description", "Pick up milk after work"))
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().location("/todos/1")
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.title").isEqualTo("Buy milk")
				.jsonPath("$.description").isEqualTo("Pick up milk after work")
				.jsonPath("$.completed").isEqualTo(false);

		restTestClient.get().uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].id").isEqualTo(1)
				.jsonPath("$[0].title").isEqualTo("Buy milk");

		restTestClient.get().uri("/todos/1")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.title").isEqualTo("Buy milk");

		restTestClient.put().uri("/todos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("title", "Buy oat milk"))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.title").isEqualTo("Buy oat milk")
				.jsonPath("$.completed").isEqualTo(false);

		restTestClient.delete().uri("/todos/1")
				.exchange()
				.expectStatus().isNoContent()
				.expectBody().isEmpty();

		restTestClient.get().uri("/todos/1")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void createReturns400WhenTitleIsBlank() {
		restTestClient.post().uri("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("title", " "))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void getReturns404ForUnknownTodo() {
		restTestClient.get().uri("/todos/999999")
				.exchange()
				.expectStatus().isNotFound();
	}

}
