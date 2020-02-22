package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.catalincretu.spacexapiexplorer.spacex.SpacexRocketLaunchResponse;
import com.github.catalincretu.spacexapiexplorer.spacex.SpacexRocketResponse;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
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

  static RocketResponse toRocketResponse(final Object[] responses) {
    SpacexRocketResponse spacexRocketResponse = (SpacexRocketResponse) responses[0];
    @SuppressWarnings("unchecked")
    List<SpacexRocketLaunchResponse> nextRocketLaunchResponses = (List<SpacexRocketLaunchResponse>) responses[1];
    @SuppressWarnings("unchecked")
    List<SpacexRocketLaunchResponse> pastRocketLaunchResponses = (List<SpacexRocketLaunchResponse>) responses[2];

    return RocketResponse.builder()
        .id(spacexRocketResponse.getRocketId())
        .rocketName(spacexRocketResponse.getRocketName())
        .description(spacexRocketResponse.getDescription())
        .active(spacexRocketResponse.getActive())
        .boosters(spacexRocketResponse.getBoosters())
        .stages(spacexRocketResponse.getStages())
        .successRatePct(spacexRocketResponse.getSuccessRatePct())
        .launches(toFirstLaunches(nextRocketLaunchResponses, pastRocketLaunchResponses))
        .build();
  }

  private static LaunchesView toFirstLaunches(
      final List<SpacexRocketLaunchResponse> nextSpacexLaunchResponses,
      final List<SpacexRocketLaunchResponse> pastSpacexLaunchResponses
  ) {
    List<LaunchView> nextLaunches = toNextLaunches(nextSpacexLaunchResponses);
    List<LaunchView> pastLaunches = toPastLaunches(pastSpacexLaunchResponses);

    return new LaunchesView(nextLaunches, pastLaunches);
  }

  private static List<LaunchView> toNextLaunches(final List<SpacexRocketLaunchResponse> spacexLaunchResponses) {
    return toLaunch(spacexLaunchResponses, comparing(SpacexRocketLaunchResponse::getLaunchDateUtc));
  }

  private static List<LaunchView> toPastLaunches(final List<SpacexRocketLaunchResponse> spacexLaunchResponses) {
    return toLaunch(spacexLaunchResponses, comparing(SpacexRocketLaunchResponse::getLaunchDateUtc).reversed());
  }

  private static List<LaunchView> toLaunch(
      final List<SpacexRocketLaunchResponse> spacexLaunchResponses,
      final Comparator<SpacexRocketLaunchResponse> comparator) {
    return spacexLaunchResponses.stream()
        .sorted(comparator)
        .limit(3)
        .map(Converters::toLaunchView)
        .collect(toList());
  }

  private static LaunchView toLaunchView(final SpacexRocketLaunchResponse spacexLaunchResponse) {
    return LaunchView.builder()
        .flightNumber(spacexLaunchResponse.getFlightNumber())
        .missionName(spacexLaunchResponse.getMissionName())
        .launchDate(spacexLaunchResponse.getLaunchDateUtc())
        .build();
  }
}
