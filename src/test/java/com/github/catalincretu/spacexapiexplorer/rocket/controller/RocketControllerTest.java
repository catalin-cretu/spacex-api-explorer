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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@AutoConfigureWebTestClient
@SuppressWarnings("squid:S2696")
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
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
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

  @Test
  @DisplayName("GET /api/rockets/{rocketId} - by ID - returns single rocket response")
  void getRocketById() {
    stubFor(get("/v3/rockets/falcon9")
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBodyFile("rocket_falcon9.json")));

    webTestClient.get()
        .uri("/api/rockets/falcon9")
        .exchange()

        .expectStatus().isOk()
        .expectBody()

        .jsonPath("id").isEqualTo("falcon9")
        .jsonPath("rocketName").isEqualTo("Falcon 9")
        .jsonPath("description").value(containsString("manufactured by SpaceX"))
        .jsonPath("active").isEqualTo(true)
        .jsonPath("boosters").isEqualTo(0)
        .jsonPath("stages").isEqualTo(2)
        .jsonPath("successRatePct").isEqualTo(97);

    verify(
        getRequestedFor(urlEqualTo("/v3/rockets/falcon9")));
  }
}