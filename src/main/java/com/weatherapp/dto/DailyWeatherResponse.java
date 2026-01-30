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
public class DailyWeatherResponse {

    @JsonProperty("forecast_count")
    private Integer forecastCount;
    
    private List<DailyForecast> daily;
    
    @JsonProperty("data_source")
    private String dataSource;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyForecast {
        
        @JsonProperty("forecast_date")
        private LocalDateTime forecastDate;
        
        private Temperature temperature;
        
        @JsonProperty("feels_like")
        private FeelsLike feelsLike;
        
        private Integer pressure;
        private Integer humidity;
        
        @JsonProperty("dew_point")
        private Double dewPoint;
        
        @JsonProperty("wind_speed")
        private Double windSpeed;
        
        @JsonProperty("wind_deg")
        private Integer windDeg;
        
        @JsonProperty("wind_gust")
        private Double windGust;
        
        private Integer clouds;
        
        @JsonProperty("pop")
        private Double probabilityOfPrecipitation;
        
        private Double rain;
        private Double snow;
        
        private WeatherCondition weather;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Temperature {
        private Double morning;
        private Double day;
        private Double evening;
        private Double night;
        private Double min;
        private Double max;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeelsLike {
        private Double morning;
        private Double day;
        private Double evening;
        private Double night;
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
