package com.github.catalincretu.spacexapiexplorer.spacex.service;

import com.github.catalincretu.spacexapiexplorer.rocket.controller.RocketResponse;
import com.github.catalincretu.spacexapiexplorer.rocket.controller.RocketSummaryResponse;
import com.github.catalincretu.spacexapiexplorer.spacex.client.SpacexClient;
import com.github.catalincretu.spacexapiexplorer.spacex.client.SpacexRocketLaunchResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class SpacexService {

  private final SpacexClient spacexClient;

  public Flux<RocketSummaryResponse> findAllRockets() {
    return spacexClient.findAllRockets()
        .map(Converters::toRocketsResponse);
  }

  public Mono<RocketResponse> findRocket(final String rocketId) {
    Mono<List<SpacexRocketLaunchResponse>> nextLaunchesResponse = findNextRocketLaunches(rocketId).collect(toList());
    Mono<List<SpacexRocketLaunchResponse>> pastLaunchesResponse = findPastRocketLaunches(rocketId).collect(toList());

    return spacexClient.findRocket(rocketId)
        .zipWith(nextLaunchesResponse, Converters::toRocketResponseWithNextLaunches)
        .zipWith(pastLaunchesResponse, Converters::toRocketResponseWithPastLaunches);
  }

  private Flux<SpacexRocketLaunchResponse> findNextRocketLaunches(final String rocketId) {
    return spacexClient.findNextRocketLaunches(rocketId);
  }

  private Flux<SpacexRocketLaunchResponse> findPastRocketLaunches(final String rocketId) {
    return spacexClient.findPastRocketLaunches(rocketId);
  }
}
