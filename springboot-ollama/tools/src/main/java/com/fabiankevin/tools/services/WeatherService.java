package com.fabiankevin.tools.services;

import com.fabiankevin.tools.enums.Unit;
import com.fabiankevin.tools.services.dto.WeatherRequest;
import com.fabiankevin.tools.services.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class WeatherService implements Function<WeatherRequest, WeatherResponse> {
    public WeatherResponse apply(WeatherRequest request) {
        log.info("WeatherRequest: {}", request);
        return new WeatherResponse(30.0, Unit.C);
    }
}