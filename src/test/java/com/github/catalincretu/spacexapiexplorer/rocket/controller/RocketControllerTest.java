package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest
@AutoConfigureWebTestClient
class RocketControllerTest {

  private static WireMockServer wireMockServer;

  @Autowired
  private WebTestClient webTestClient;
  @Value("${wiremock.port}")
  private Integer wiremockPort;

  @BeforeEach
  void setupWireMock() {
    if (wireMockServer == null) {
      wireMockServer = new WireMockServer(wiremockPort);
      wireMockServer.start();

      WireMock.configureFor(wiremockPort);
    }
  }

  @AfterAll
  static void tearDownWireMock() {
    wireMockServer.stop();
  }

  @Test
  @DisplayName("GET /api/rockets - returns rockets summary response")
  void getAllRockets() {
    stubFor(get("/v3/rockets")
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("rockets.json")));

    webTestClient.get()
        .uri("/api/rockets")
        .exchange()

        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$[*].id").value(hasItems("falcon1", "falcon9"));

    verify(
        getRequestedFor(urlEqualTo("/v3/rockets")));
  }
}