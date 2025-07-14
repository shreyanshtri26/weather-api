package com.weather.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openweather.api.base}")
    private String weatherApiBase;

    @Value("${openweather.api.key}")
    private String apiKey;

    public String getWeatherJson(Double lat, Double lon) {
        String url = String.format("%s/weather?lat=%s&lon=%s&appid=%s&units=metric", weatherApiBase, lat, lon, apiKey);
        return restTemplate.getForObject(url, String.class);
    }
}