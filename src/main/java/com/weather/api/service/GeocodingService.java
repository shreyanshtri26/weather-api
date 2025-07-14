package com.weather.api.service;

import com.weather.api.model.PincodeLocation;
import com.weather.api.repository.PincodeLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeocodingService {
    private final PincodeLocationRepository pincodeLocationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openweather.geocoding.base}")
    private String geocodingBaseUrl;

    @Value("${openweather.api.key}")
    private String apiKey;

    public PincodeLocation getLatLong(String pincode) {
        Optional<PincodeLocation> cached = pincodeLocationRepository.findByPincode(pincode);
        if (cached.isPresent()) {
            return cached.get();
        }
        // Call OpenWeatherMap Geocoding API
        String url = String.format("%s?zip=%s,IN&appid=%s", geocodingBaseUrl, pincode, apiKey);
        var response = restTemplate.getForObject(url, GeocodingResponse.class);
        if (response != null && response.getLat() != null && response.getLon() != null) {
            PincodeLocation location = PincodeLocation.builder()
                    .pincode(pincode)
                    .latitude(response.getLat())
                    .longitude(response.getLon())
                    .build();
            pincodeLocationRepository.save(location);
            return location;
        }
        throw new RuntimeException("Could not fetch lat/long for pincode: " + pincode);
    }

    // Helper class for API response
    private static class GeocodingResponse {
        private Double lat;
        private Double lon;

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }
    }
}