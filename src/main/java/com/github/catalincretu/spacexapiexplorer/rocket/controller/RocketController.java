package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.catalincretu.spacexapiexplorer.spacex.service.SpacexService;
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

  private final SpacexService spacexService;

  @GetMapping
  public Flux<RocketSummaryResponse> getAll() {
    log.debug("Find all rockets");

    return spacexService.findAllRockets();
  }

  @GetMapping("/{rocketId}")
  public Mono<RocketResponse> getOne(@PathVariable String rocketId) {
    log.info("Find rocket with id [{}]", rocketId);

    return spacexService.findRocket(rocketId)
        .doOnSuccess(RocketController::logResponse);
  }

  private static void logResponse(final RocketResponse response) {
    log.debug("Found rocket [{}] with {} past launches and {} next launches",
        response.getId(),
        response.getLaunches().getNext().size(),
        response.getLaunches().getPast().size());
  }
}
