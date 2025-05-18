package com.fabiankevin.tools.services.dto;

import com.fabiankevin.tools.enums.Unit;

public record WeatherResponse(double temp, Unit unit) {}