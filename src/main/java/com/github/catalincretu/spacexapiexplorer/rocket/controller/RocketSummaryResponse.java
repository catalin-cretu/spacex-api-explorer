package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RocketSummaryResponse {
  String id;

  String description;
  Boolean active;
  String rocketName;
}
