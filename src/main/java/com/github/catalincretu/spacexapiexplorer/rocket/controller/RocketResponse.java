package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RocketResponse {
  String id;

  String description;
  Boolean active;
  String rocketName;

  Integer stages;
  Integer boosters;
  Integer successRatePct;

  LaunchesView launches;
}
