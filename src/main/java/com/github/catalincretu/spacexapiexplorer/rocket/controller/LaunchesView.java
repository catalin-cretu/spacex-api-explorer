package com.github.catalincretu.spacexapiexplorer.rocket.controller;

import lombok.Value;

import java.util.List;

@Value
public class LaunchesView {
  List<LaunchView> next;
}
