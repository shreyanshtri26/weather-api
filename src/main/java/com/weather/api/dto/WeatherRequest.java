package com.weather.api.dto;

import lombok.Data;

@Data
public class WeatherRequest {
    private String pincode;
    private String for_date; // ISO format (yyyy-MM-dd)
}