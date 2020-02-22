package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class LaunchView {
  Integer flightNumber;
  String missionName;
  Instant launchDate;
}
