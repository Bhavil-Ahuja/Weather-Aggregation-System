package com.weatherapp.dto.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lon")
    private Double longitude;

    private String timezone;

    @JsonProperty("timezone_offset")
    private Integer timezoneOffset;

    private Current current;

    private List<Hourly> hourly;

    private List<Daily> daily;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        @JsonProperty("dt")
        private Long timestamp;

        private Double temp;

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

        private List<Weather> weather;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hourly {
        @JsonProperty("dt")
        private Long timestamp;

        private Double temp;

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

        private List<Weather> weather;

        @JsonProperty("pop")
        private Double probabilityOfPrecipitation;

        private Rain rain;
        private Snow snow;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Daily {
        @JsonProperty("dt")
        private Long timestamp;

        private Temperature temp;

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

        private List<Weather> weather;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Temperature {
        private Double morn;
        private Double day;
        private Double eve;
        private Double night;
        private Double min;
        private Double max;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeelsLike {
        private Double morn;
        private Double day;
        private Double eve;
        private Double night;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rain {
        @JsonProperty("1h")
        private Double oneHour;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Snow {
        @JsonProperty("1h")
        private Double oneHour;
    }
}
