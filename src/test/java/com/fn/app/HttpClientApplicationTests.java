package com.fn.app;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNotNull;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.JsonNode;

import junit.framework.TestCase;
import junit.framework.TestSuite;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpClientApplicationTests  {

	@LocalServerPort
	private int port;
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Before
	public void before() {
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:"+port).build();
	}
	
	@Test
	public void getMusicInfo() {
		webTestClient.get()
					.uri("/MusicApp/load/love/2/10")
					.exchange()
					.expectStatus()
					.isOk()
					.expectBody(JsonNode.class)
					.consumeWith((result)->{
						JsonNode body = result.getResponseBody();
						assertThat(body.has("tracks"), isNotNull());
						assertThat(body.has("artists"), isNotNull());
					});
	}
}
