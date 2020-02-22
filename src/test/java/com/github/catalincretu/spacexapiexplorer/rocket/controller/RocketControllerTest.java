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
@SuppressWarnings({"squid:S2696", "squid:S1192"})
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
    setupGetRequestStub("/v3/rockets", "rockets.json");

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
    setupGetRequestStub("/v3/rockets/falcon9", "rocket_falcon9.json");

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

  @Test
  @DisplayName("GET /api/rockets/{rocketId} - by ID - returns single rocket and launches response")
  void getRocketByIdWithLaunches() {
    setupGetRequestStub("/v3/rockets/falcon9", "rocket_falcon9.json");
    setupGetRequestStub("/v3/launches/upcoming?rocket_id=falcon9", "launches_upcoming_falcon9.json");
    setupGetRequestStub("/v3/launches/past?rocket_id=falcon9", "launches_past_falcon9.json");

    webTestClient.get()
        .uri("/api/rockets/falcon9")
        .exchange()

        .expectStatus().isOk()
        .expectBody()

        .jsonPath("id").isEqualTo("falcon9")

        .jsonPath("launches.next.size()").isEqualTo(3)
        .jsonPath("launches.next[*].flightNumber").value(hasItems(91, 92, 93))
        .jsonPath("launches.next[*].missionName")
        .value(hasItems("CRS-20", "Starlink 5", "SAOCOM 1B & Smallsat SSO Rideshare 1"))
        .jsonPath("launches.next[*].launchDate")
        .value(hasItems("2020-03-02T06:45:00Z", "2020-03-03T00:00:00Z", "2020-03-04T00:00:00Z"))

        .jsonPath("launches.past.size()").isEqualTo(3)
        .jsonPath("launches.past[*].flightNumber").value(hasItems(90, 89, 88))
        .jsonPath("launches.past[*].missionName")
        .value(hasItems("Starlink 4", "Starlink 3", "Crew Dragon In Flight Abort Test"))
        .jsonPath("launches.past[*].launchDate")
        .value(hasItems("2020-02-17T15:05:55Z", "2020-01-29T14:06:00Z", "2020-01-19T14:00:00Z"));

    verify(
        getRequestedFor(urlEqualTo("/v3/rockets/falcon9")));
    verify(
        getRequestedFor(urlEqualTo("/v3/launches/upcoming?rocket_id=falcon9")));
    verify(
        getRequestedFor(urlEqualTo("/v3/launches/past?rocket_id=falcon9")));
  }

  private static void setupGetRequestStub(final String url, final String fileName) {
    stubFor(get(url)
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBodyFile(fileName)));
  }
}