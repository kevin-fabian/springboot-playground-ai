package com.fabiankevin.tools.services.dto;

import org.springframework.format.annotation.DurationFormat;

public record WeatherRequest(String location, DurationFormat.Unit unit) {}
