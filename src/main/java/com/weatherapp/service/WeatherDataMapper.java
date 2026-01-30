package com.weatherapp.service;

import com.weatherapp.dto.CurrentWeatherResponse;
import com.weatherapp.dto.DailyWeatherResponse;
import com.weatherapp.dto.HourlyWeatherResponse;
import com.weatherapp.dto.openweather.OpenWeatherResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherDataMapper {

    private LocalDateTime convertToLocalDateTime(Long timestamp) {
        return timestamp != null 
                ? LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String getWeatherMain(List<OpenWeatherResponse.Weather> weatherList) {
        return weatherList != null && !weatherList.isEmpty() 
                ? weatherList.get(0).getMain() 
                : null;
    }

    private String getWeatherDescription(List<OpenWeatherResponse.Weather> weatherList) {
        return weatherList != null && !weatherList.isEmpty() 
                ? weatherList.get(0).getDescription() 
                : null;
    }

    private String getWeatherIcon(List<OpenWeatherResponse.Weather> weatherList) {
        return weatherList != null && !weatherList.isEmpty() 
                ? weatherList.get(0).getIcon() 
                : null;
    }

    private Double getRain1h(OpenWeatherResponse.Rain rain) {
        return rain != null ? rain.getOneHour() : null;
    }

    private Double getSnow1h(OpenWeatherResponse.Snow snow) {
        return snow != null ? snow.getOneHour() : null;
    }

    public CurrentWeatherResponse mapToCurrentWeatherResponseFromApi(
            OpenWeatherResponse.Current current,
            BigDecimal latitude,
            BigDecimal longitude) {
        
        return CurrentWeatherResponse.builder()
                .timestamp(convertToLocalDateTime(current.getTimestamp()))
                .temperature(current.getTemp())
                .feelsLike(current.getFeelsLike())
                .pressure(current.getPressure())
                .humidity(current.getHumidity())
                .dewPoint(current.getDewPoint())
                .clouds(current.getClouds())
                .visibility(current.getVisibility())
                .windSpeed(current.getWindSpeed())
                .windDeg(current.getWindDeg())
                .windGust(current.getWindGust())
                .weather(CurrentWeatherResponse.WeatherCondition.builder()
                        .main(getWeatherMain(current.getWeather()))
                        .description(getWeatherDescription(current.getWeather()))
                        .icon(getWeatherIcon(current.getWeather()))
                        .build())
                .dataSource("external_api")
                .build();
    }

    public HourlyWeatherResponse mapToHourlyWeatherResponseFromApi(
            List<OpenWeatherResponse.Hourly> hourlyList,
            BigDecimal latitude,
            BigDecimal longitude) {
        
        List<HourlyWeatherResponse.HourlyForecast> forecasts = hourlyList.stream()
                .limit(24)
                .map(hourly -> HourlyWeatherResponse.HourlyForecast.builder()
                        .forecastTime(convertToLocalDateTime(hourly.getTimestamp()))
                        .temperature(hourly.getTemp())
                        .feelsLike(hourly.getFeelsLike())
                        .pressure(hourly.getPressure())
                        .humidity(hourly.getHumidity())
                        .dewPoint(hourly.getDewPoint())
                        .clouds(hourly.getClouds())
                        .visibility(hourly.getVisibility())
                        .windSpeed(hourly.getWindSpeed())
                        .windDeg(hourly.getWindDeg())
                        .windGust(hourly.getWindGust())
                        .weather(HourlyWeatherResponse.WeatherCondition.builder()
                                .main(getWeatherMain(hourly.getWeather()))
                                .description(getWeatherDescription(hourly.getWeather()))
                                .icon(getWeatherIcon(hourly.getWeather()))
                                .build())
                        .probabilityOfPrecipitation(hourly.getProbabilityOfPrecipitation())
                        .rain1h(getRain1h(hourly.getRain()))
                        .snow1h(getSnow1h(hourly.getSnow()))
                        .build())
                .collect(Collectors.toList());
        
        return HourlyWeatherResponse.builder()
                .forecastCount(forecasts.size())
                .hourly(forecasts)
                .dataSource("external_api")
                .build();
    }

    public DailyWeatherResponse mapToDailyWeatherResponseFromApi(
            List<OpenWeatherResponse.Daily> dailyList,
            BigDecimal latitude,
            BigDecimal longitude) {
        
        List<DailyWeatherResponse.DailyForecast> forecasts = dailyList.stream()
                .limit(7)
                .map(daily -> DailyWeatherResponse.DailyForecast.builder()
                        .forecastDate(convertToLocalDateTime(daily.getTimestamp()))
                        .temperature(DailyWeatherResponse.Temperature.builder()
                                .day(daily.getTemp() != null ? daily.getTemp().getDay() : null)
                                .min(daily.getTemp() != null ? daily.getTemp().getMin() : null)
                                .max(daily.getTemp() != null ? daily.getTemp().getMax() : null)
                                .night(daily.getTemp() != null ? daily.getTemp().getNight() : null)
                                .evening(daily.getTemp() != null ? daily.getTemp().getEve() : null)
                                .morning(daily.getTemp() != null ? daily.getTemp().getMorn() : null)
                                .build())
                        .feelsLike(DailyWeatherResponse.FeelsLike.builder()
                                .day(daily.getFeelsLike() != null ? daily.getFeelsLike().getDay() : null)
                                .night(daily.getFeelsLike() != null ? daily.getFeelsLike().getNight() : null)
                                .evening(daily.getFeelsLike() != null ? daily.getFeelsLike().getEve() : null)
                                .morning(daily.getFeelsLike() != null ? daily.getFeelsLike().getMorn() : null)
                                .build())
                        .pressure(daily.getPressure())
                        .humidity(daily.getHumidity())
                        .dewPoint(daily.getDewPoint())
                        .windSpeed(daily.getWindSpeed())
                        .windDeg(daily.getWindDeg())
                        .windGust(daily.getWindGust())
                        .clouds(daily.getClouds())
                        .probabilityOfPrecipitation(daily.getProbabilityOfPrecipitation())
                        .rain(daily.getRain())
                        .snow(daily.getSnow())
                        .weather(DailyWeatherResponse.WeatherCondition.builder()
                                .main(getWeatherMain(daily.getWeather()))
                                .description(getWeatherDescription(daily.getWeather()))
                                .icon(getWeatherIcon(daily.getWeather()))
                                .build())
                        .build())
                .collect(Collectors.toList());
        
        return DailyWeatherResponse.builder()
                .forecastCount(forecasts.size())
                .daily(forecasts)
                .dataSource("external_api")
                .build();
    }
}
