package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.catalincretu.spacexapiexplorer.spacex.SpacexRocketLaunchResponse;
import com.github.catalincretu.spacexapiexplorer.spacex.SpacexRocketResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class Converters {

  static RocketSummaryResponse toRocketsResponse(final SpacexRocketResponse spacexRocketResponse) {
    return RocketSummaryResponse.builder()
        .id(spacexRocketResponse.getRocketId())
        .active(spacexRocketResponse.getActive())
        .description(spacexRocketResponse.getDescription())
        .rocketName(spacexRocketResponse.getRocketName())
        .build();
  }

  static RocketResponse toRocketResponse(
      final SpacexRocketResponse spacexRocketResponse,
      final List<SpacexRocketLaunchResponse> spacexRocketLaunchResponses
  ) {

    return RocketResponse.builder()
        .id(spacexRocketResponse.getRocketId())
        .rocketName(spacexRocketResponse.getRocketName())
        .description(spacexRocketResponse.getDescription())
        .active(spacexRocketResponse.getActive())
        .boosters(spacexRocketResponse.getBoosters())
        .stages(spacexRocketResponse.getStages())
        .successRatePct(spacexRocketResponse.getSuccessRatePct())
        .launches(toFirstLaunches(spacexRocketLaunchResponses))
        .build();
  }

  private static LaunchesView toFirstLaunches(final List<SpacexRocketLaunchResponse> spacexRocketLaunchResponses) {
    List<LaunchView> nextLaunches = spacexRocketLaunchResponses.stream()
        .sorted(comparing(SpacexRocketLaunchResponse::getLaunchDateUtc))
        .limit(3)
        .map(Converters::toFirstLaunches)
        .collect(toList());

    return new LaunchesView(nextLaunches);
  }

  private static LaunchView toFirstLaunches(final SpacexRocketLaunchResponse spacexLaunchResponse) {
    return LaunchView.builder()
        .flightNumber(spacexLaunchResponse.getFlightNumber())
        .missionName(spacexLaunchResponse.getMissionName())
        .launchDate(spacexLaunchResponse.getLaunchDateUtc())
        .build();
  }
}
