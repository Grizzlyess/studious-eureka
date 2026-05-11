package com.grizzlyess.studious_eureka;

import com.grizzlyess.studious_eureka.entity.Todo;
import com.grizzlyess.studious_eureka.repository.TodoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudiousEurekaApplicationTests {
	@Autowired
	private TodoRepository repository;

	@Autowired
	private MockMvc mockMvc;

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		this.webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
	}

	@Test
	void testCreateTodoSuccess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);

		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(1)
				.jsonPath("$[0].nome").isEqualTo(todo.getNome())
				.jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
				.jsonPath("$[0].realizado").isEqualTo(todo.isRealizado())
				.jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());


	}

	@Test
	void testCreateTodoFailure() {
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(
						new Todo("", "", false, 0)
				).exchange()
				.expectStatus().isBadRequest();


	}

	@Test
	void testDeleteTodoSuccess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);

		Todo todoSalvo = repository.save(todo);

		UUID id = UUID.randomUUID();

		webTestClient
				.delete()
				.uri("/todos/{id}", id)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray();
	}

}
