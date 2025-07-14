package com.weather.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.api.dto.WeatherRequest;
import com.weather.api.dto.WeatherResponse;
import com.weather.api.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetWeather() throws Exception {
        WeatherRequest request = new WeatherRequest();
        request.setPincode("411014");
        request.setFor_date("2020-10-15");
        WeatherResponse response = WeatherResponse.builder()
                .pincode("411014")
                .for_date("2020-10-15")
                .latitude(18.5)
                .longitude(73.9)
                .weatherData("{\"main\":{\"temp\":25}}\n")
                .build();
        when(weatherService.getWeatherInfo(any())).thenReturn(response);

        mockMvc.perform(post("/api/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode").value("411014"))
                .andExpect(jsonPath("$.for_date").value("2020-10-15"))
                .andExpect(jsonPath("$.latitude").value(18.5))
                .andExpect(jsonPath("$.longitude").value(73.9));
    }
}