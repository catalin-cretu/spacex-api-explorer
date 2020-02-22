package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.catalincretu.spacexapiexplorer.spacex.SpacexClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/rockets")
public class RocketController {

  private final SpacexClient spacexClient;

  @GetMapping
  public Flux<RocketSummaryResponse> getAll() {
    log.debug("Find all rockets");

    return spacexClient.findAllRockets()
        .map(Converters::toRocketsResponse);
  }

  @GetMapping("/{rocketId}")
  public Mono<RocketResponse> getOne(@PathVariable String rocketId) {
    log.info("Find rocket with id [{}]", rocketId);

    return spacexClient.findRocket(rocketId)
        .map(Converters::toRocketResponse);
  }
}
