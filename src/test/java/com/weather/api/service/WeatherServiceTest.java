package com.weather.api.service;

import com.weather.api.dto.WeatherRequest;
import com.weather.api.dto.WeatherResponse;
import com.weather.api.model.PincodeLocation;
import com.weather.api.model.WeatherInfo;
import com.weather.api.repository.WeatherInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {
    @Mock
    private WeatherInfoRepository weatherInfoRepository;
    @Mock
    private GeocodingService geocodingService;
    @Mock
    private OpenWeatherService openWeatherService;
    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherService(weatherInfoRepository, geocodingService, openWeatherService);
    }

    @Test
    void testGetWeatherInfo_NotCached() {
        WeatherRequest request = new WeatherRequest();
        request.setPincode("411014");
        request.setFor_date("2020-10-15");
        LocalDate date = LocalDate.parse("2020-10-15");

        when(weatherInfoRepository.findByPincodeAndDate("411014", date)).thenReturn(Optional.empty());
        when(geocodingService.getLatLong("411014")).thenReturn(
                PincodeLocation.builder().pincode("411014").latitude(18.5).longitude(73.9).build());
        when(openWeatherService.getWeatherJson(18.5, 73.9)).thenReturn("{\"main\":{\"temp\":25}}\n");
        when(weatherInfoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        WeatherResponse response = weatherService.getWeatherInfo(request);
        assertEquals("411014", response.getPincode());
        assertEquals("2020-10-15", response.getFor_date());
        assertEquals(18.5, response.getLatitude());
        assertEquals(73.9, response.getLongitude());
        assertTrue(response.getWeatherData().toString().contains("temp"));
    }

    @Test
    void testGetWeatherInfo_Cached() {
        WeatherRequest request = new WeatherRequest();
        request.setPincode("411014");
        request.setFor_date("2020-10-15");
        LocalDate date = LocalDate.parse("2020-10-15");
        WeatherInfo info = WeatherInfo.builder()
                .pincode("411014")
                .date(date)
                .latitude(18.5)
                .longitude(73.9)
                .weatherJson("{\"main\":{\"temp\":25}}\n")
                .build();
        when(weatherInfoRepository.findByPincodeAndDate("411014", date)).thenReturn(Optional.of(info));

        WeatherResponse response = weatherService.getWeatherInfo(request);
        assertEquals("411014", response.getPincode());
        assertEquals("2020-10-15", response.getFor_date());
        assertEquals(18.5, response.getLatitude());
        assertEquals(73.9, response.getLongitude());
        assertTrue(response.getWeatherData().toString().contains("temp"));
    }
}