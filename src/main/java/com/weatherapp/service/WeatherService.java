package com.weatherapp.service;

import com.weatherapp.client.OpenWeatherClient;
import com.weatherapp.dto.CurrentWeatherResponse;
import com.weatherapp.dto.DailyWeatherResponse;
import com.weatherapp.dto.HourlyWeatherResponse;
import com.weatherapp.dto.openweather.OpenWeatherResponse;
import com.weatherapp.exception.InvalidLocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;
    private final WeatherDataMapper mapper;

    @Cacheable(value = "currentWeather", key = "#latitude + ':' + #longitude")
    public CurrentWeatherResponse getCurrentWeather(BigDecimal latitude, BigDecimal longitude) {
        validateLocation(latitude, longitude);
        log.info("CACHE MISS: Fetching current weather from external API for location: ({}, {})", 
                latitude, longitude);
        
        OpenWeatherResponse apiResponse = openWeatherClient.fetchWeatherData(latitude, longitude);
        
        if (apiResponse.getCurrent() == null) {
            throw new InvalidLocationException("No current weather data available for this location");
        }

        return mapper.mapToCurrentWeatherResponseFromApi(apiResponse.getCurrent(), latitude, longitude);
    }

    @Cacheable(value = "hourlyForecast", key = "#latitude + ':' + #longitude")
    public HourlyWeatherResponse getHourlyForecast(BigDecimal latitude, BigDecimal longitude) {
        validateLocation(latitude, longitude);
        log.info("CACHE MISS: Fetching hourly forecast from external API for location: ({}, {})", 
                latitude, longitude);
        
        OpenWeatherResponse apiResponse = openWeatherClient.fetchWeatherData(latitude, longitude);
        
        if (apiResponse.getHourly() == null || apiResponse.getHourly().isEmpty()) {
            throw new InvalidLocationException("No hourly forecast data available for this location");
        }

        return mapper.mapToHourlyWeatherResponseFromApi(apiResponse.getHourly(), latitude, longitude);
    }

    @Cacheable(value = "dailyForecast", key = "#latitude + ':' + #longitude")
    public DailyWeatherResponse getDailyForecast(BigDecimal latitude, BigDecimal longitude) {
        validateLocation(latitude, longitude);
        log.info("CACHE MISS: Fetching daily forecast from external API for location: ({}, {})", 
                latitude, longitude);
        
        OpenWeatherResponse apiResponse = openWeatherClient.fetchWeatherData(latitude, longitude);
        
        if (apiResponse.getDaily() == null || apiResponse.getDaily().isEmpty()) {
            throw new InvalidLocationException("No daily forecast data available for this location");
        }

        return mapper.mapToDailyWeatherResponseFromApi(apiResponse.getDaily(), latitude, longitude);
    }

    private void validateLocation(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new InvalidLocationException("Latitude and longitude are required");
        }

        if (latitude.compareTo(new BigDecimal("-90")) < 0 || 
            latitude.compareTo(new BigDecimal("90")) > 0) {
            throw new InvalidLocationException(
                    "Invalid latitude: must be between -90 and 90"
            );
        }

        if (longitude.compareTo(new BigDecimal("-180")) < 0 || 
            longitude.compareTo(new BigDecimal("180")) > 0) {
            throw new InvalidLocationException(
                    "Invalid longitude: must be between -180 and 180"
            );
        }
    }
}
