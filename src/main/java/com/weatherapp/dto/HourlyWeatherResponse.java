package com.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeatherResponse {

    @JsonProperty("forecast_count")
    private Integer forecastCount;
    
    private List<HourlyForecast> hourly;
    
    @JsonProperty("data_source")
    private String dataSource;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyForecast {
        
        @JsonProperty("forecast_time")
        private LocalDateTime forecastTime;
        
        private Double temperature;
        
        @JsonProperty("feels_like")
        private Double feelsLike;
        
        private Integer pressure;
        private Integer humidity;
        
        @JsonProperty("dew_point")
        private Double dewPoint;
        
        private Integer clouds;
        private Double visibility;
        
        @JsonProperty("wind_speed")
        private Double windSpeed;
        
        @JsonProperty("wind_deg")
        private Integer windDeg;
        
        @JsonProperty("wind_gust")
        private Double windGust;
        
        private WeatherCondition weather;
        
        @JsonProperty("pop")
        private Double probabilityOfPrecipitation;
        
        @JsonProperty("rain_1h")
        private Double rain1h;
        
        @JsonProperty("snow_1h")
        private Double snow1h;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherCondition {
        private String main;
        private String description;
        private String icon;
    }
}
