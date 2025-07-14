package com.weather.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.api.dto.WeatherRequest;
import com.weather.api.dto.WeatherResponse;
import com.weather.api.model.PincodeLocation;
import com.weather.api.model.WeatherInfo;
import com.weather.api.repository.WeatherInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherInfoRepository weatherInfoRepository;
    private final GeocodingService geocodingService;
    private final OpenWeatherService openWeatherService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "weather", key = "#request.pincode + '-' + #request.for_date")
    public WeatherResponse getWeatherInfo(WeatherRequest request) {
        LocalDate date = LocalDate.parse(request.getFor_date());
        Optional<WeatherInfo> cached = weatherInfoRepository.findByPincodeAndDate(request.getPincode(), date);
        if (cached.isPresent()) {
            WeatherInfo info = cached.get();
            return WeatherResponse.builder()
                    .pincode(info.getPincode())
                    .for_date(info.getDate().toString())
                    .latitude(info.getLatitude())
                    .longitude(info.getLongitude())
                    .weatherData(parseWeatherJson(info.getWeatherJson()))
                    .build();
        }
        // Get lat/lon
        PincodeLocation location = geocodingService.getLatLong(request.getPincode());
        // Get weather JSON
        String weatherJson = openWeatherService.getWeatherJson(location.getLatitude(), location.getLongitude());
        // Save to DB
        WeatherInfo info = WeatherInfo.builder()
                .pincode(request.getPincode())
                .date(date)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .weatherJson(weatherJson)
                .build();
        weatherInfoRepository.save(info);
        return WeatherResponse.builder()
                .pincode(request.getPincode())
                .for_date(request.getFor_date())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .weatherData(parseWeatherJson(weatherJson))
                .build();
    }

    private Object parseWeatherJson(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return json;
        }
    }
}