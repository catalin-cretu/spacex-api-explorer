package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import com.github.catalincretu.spacexapiexplorer.spacex.SpacexRocketResponse;
import lombok.experimental.UtilityClass;

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
}
