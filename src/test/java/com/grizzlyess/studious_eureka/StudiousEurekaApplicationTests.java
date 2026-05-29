package com.grizzlyess.studious_eureka;

import com.grizzlyess.studious_eureka.entity.Todo;
import com.grizzlyess.studious_eureka.repository.TodoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
		repository.deleteAll();
	}

	@Test
	void testCreateTodoSuccess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);

		webTestClient
				.post()
				.uri("/todos")
				.contentType(MediaType.APPLICATION_JSON)
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
	void testUpdateTodoSuccess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);
		var atualizado = new Todo("novo nome", "nova desc", true, 2);
		atualizado.setId(todo.getId());
		webTestClient
				.put()
				.uri("/todos")
				.contentType(MediaType.APPLICATION_JSON)
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
	void testListTodoSuccess() {

		var todo1 = new Todo("todo 1", "desc todo 1", false, 1);
		var todo2 = new Todo("todo 2", "desc todo 2", true, 2);

		repository.save(todo1);
		repository.save(todo2);

		webTestClient
				.get()
				.uri("/todos")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(2)
				.jsonPath("$[0].nome").isEqualTo("todo 1")
				.jsonPath("$[0].descricao").isEqualTo("desc todo 1")
				.jsonPath("$[0].realizado").isEqualTo(false)
				.jsonPath("$[0].prioridade").isEqualTo(1)
				.jsonPath("$[1].nome").isEqualTo("todo 2")
				.jsonPath("$[1].descricao").isEqualTo("desc todo 2")
				.jsonPath("$[1].realizado").isEqualTo(true)
				.jsonPath("$[1].prioridade").isEqualTo(2);


	}

	@Test
	void testDeleteTodoSuccess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);

		Todo todoSalvo = repository.save(todo);

		var id = todoSalvo.getId();

		webTestClient
				.delete()
				.uri("/todos/{id}", id)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray();
	}

}
