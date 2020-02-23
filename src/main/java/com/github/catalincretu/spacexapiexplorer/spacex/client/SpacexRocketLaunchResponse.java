package com.github.catalincretu.spacexapiexplorer.spacex.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class SpacexRocketLaunchResponse {
  private String missionName;
  private Integer flightNumber;
  private Instant launchDateUtc;

  private Boolean isTentative;
  private String tentativeMaxPrecision;
}
