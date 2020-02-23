package com.github.catalincretu.spacexapiexplorer.spacex.service;

import com.github.catalincretu.spacexapiexplorer.rocket.controller.LaunchView;
import com.github.catalincretu.spacexapiexplorer.rocket.controller.LaunchesView;
import com.github.catalincretu.spacexapiexplorer.rocket.controller.RocketResponse;
import com.github.catalincretu.spacexapiexplorer.rocket.controller.RocketSummaryResponse;
import com.github.catalincretu.spacexapiexplorer.spacex.client.SpacexRocketLaunchResponse;
import com.github.catalincretu.spacexapiexplorer.spacex.client.SpacexRocketResponse;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class Converters {

  public static RocketSummaryResponse toRocketsResponse(final SpacexRocketResponse spacexRocketResponse) {
    return RocketSummaryResponse.builder()
        .id(spacexRocketResponse.getRocketId())
        .active(spacexRocketResponse.getActive())
        .description(spacexRocketResponse.getDescription())
        .rocketName(spacexRocketResponse.getRocketName())
        .build();
  }

  static RocketResponse toRocketResponseWithNextLaunches(
      final SpacexRocketResponse spacexRocketResponse,
      final List<SpacexRocketLaunchResponse> nextLaunches
  ) {
    return RocketResponse.builder()
        .id(spacexRocketResponse.getRocketId())
        .rocketName(spacexRocketResponse.getRocketName())
        .active(spacexRocketResponse.getActive())
        .stages(spacexRocketResponse.getStages())
        .boosters(spacexRocketResponse.getBoosters())
        .description(spacexRocketResponse.getDescription())
        .successRatePct(spacexRocketResponse.getSuccessRatePct())
        .launches(new LaunchesView(toNextLaunches(nextLaunches), emptyList()))
        .build();
  }

  static RocketResponse toRocketResponseWithPastLaunches(
      final RocketResponse rocketResponse,
      final List<SpacexRocketLaunchResponse> pastLaunches
  ) {
    return RocketResponse.builder()
        .id(rocketResponse.getId())
        .rocketName(rocketResponse.getRocketName())
        .description(rocketResponse.getDescription())
        .active(rocketResponse.getActive())
        .stages(rocketResponse.getStages())
        .boosters(rocketResponse.getBoosters())
        .successRatePct(rocketResponse.getSuccessRatePct())
        .launches(new LaunchesView(rocketResponse.getLaunches().getNext(), toPastLaunches(pastLaunches)))
        .build();
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
