package com.github.catalincretu.spacexapiexplorer.spacex.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SpacexClient {

  private final WebClient webClient;

  public SpacexClient(@Value("${client.spacex.base-url}") final String spacexBaseUrl) {
    this.webClient = WebClient.create(spacexBaseUrl);
  }

  public Flux<SpacexRocketResponse> findAllRockets() {
    log.debug("Get all SpaceX rockets");

    return webClient.get()
        .uri("/v3/rockets")
        .retrieve()
        .bodyToFlux(SpacexRocketResponse.class);
  }

  public Mono<SpacexRocketResponse> findRocket(final String rocketId) {
    log.debug("Get SpaceX rocket with id [{}]", rocketId);

    return webClient.get()
        .uri("/v3/rockets/" + rocketId)
        .retrieve()
        .bodyToMono(SpacexRocketResponse.class);
  }

  public Flux<SpacexRocketLaunchResponse> findNextRocketLaunches(final String rocketId) {
    log.debug("Get next SpaceX launches for rocket with id [{}]", rocketId);

    return webClient.get()
        .uri("/v3/launches/upcoming?rocket_id=" + rocketId)
        .retrieve()
        .bodyToFlux(SpacexRocketLaunchResponse.class);
  }

  public Flux<SpacexRocketLaunchResponse> findPastRocketLaunches(final String rocketId) {
    log.debug("Get past SpaceX launches for rocket with id [{}]", rocketId);

    return webClient.get()
        .uri("/v3/launches/past?rocket_id=" + rocketId)
        .retrieve()
        .bodyToFlux(SpacexRocketLaunchResponse.class);
  }
}
