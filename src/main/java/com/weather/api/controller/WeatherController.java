package com.weather.api.controller;

import com.weather.api.dto.WeatherRequest;
import com.weather.api.dto.WeatherResponse;
import com.weather.api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping
    public ResponseEntity<WeatherResponse> getWeather(@RequestBody WeatherRequest request) {
        WeatherResponse response = weatherService.getWeatherInfo(request);
        return ResponseEntity.ok(response);
    }
}