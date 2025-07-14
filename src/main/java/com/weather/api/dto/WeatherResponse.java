package com.weather.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponse {
    private String pincode;
    private String for_date;
    private Double latitude;
    private Double longitude;
    private Object weatherData;
}