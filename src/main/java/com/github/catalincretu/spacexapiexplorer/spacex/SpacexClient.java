package com.github.catalincretu.spacexapiexplorer.spacex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SpacexClient {

  private final WebClient webClient;

  public SpacexClient(@Value("${client.spacex.base-url}") final String spacexBaseUrl) {
    this.webClient = WebClient.create(spacexBaseUrl);
  }

  public Flux<SpacexRocketResponse> findAllRockets() {
    return webClient.get()
        .uri("/v3/rockets")
        .retrieve()
        .bodyToFlux(SpacexRocketResponse.class);
  }

  public Mono<SpacexRocketResponse> findRocket(final String rocketId) {
    return webClient.get()
        .uri("/v3/rockets/" + rocketId)
        .retrieve()
        .bodyToMono(SpacexRocketResponse.class);
  }

  public Flux<SpacexRocketLaunchResponse> findNextRocketLaunches(final String rocketId) {
    return webClient.get()
        .uri("/v3/launches/upcoming?rocket_id=" + rocketId)
        .retrieve()
        .bodyToFlux(SpacexRocketLaunchResponse.class);
  }

  public Flux<SpacexRocketLaunchResponse> findPastRocketLaunches(final String rocketId) {
    return webClient.get()
        .uri("/v3/launches/past?rocket_id=" + rocketId)
        .retrieve()
        .bodyToFlux(SpacexRocketLaunchResponse.class);
  }
}
