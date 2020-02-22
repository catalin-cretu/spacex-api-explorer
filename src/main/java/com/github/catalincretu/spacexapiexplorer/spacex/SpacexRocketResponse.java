package com.github.catalincretu.spacexapiexplorer.spacex;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class SpacexRocketResponse {
  private String rocketId;

  private String description;
  private Boolean active;
  private String rocketName;

  private Integer stages;
  private Integer boosters;
  private Integer successRatePct;
}
