package com.weatherapp.controller;

import com.weatherapp.dto.CurrentWeatherResponse;
import com.weatherapp.dto.DailyWeatherResponse;
import com.weatherapp.dto.HourlyWeatherResponse;
import com.weatherapp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Validated
@Tag(name = "Weather API", description = "Endpoints for fetching weather data and forecasts")
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(
            summary = "Get current weather",
            description = "Retrieves current weather data for the specified location. " +
                         "Data is cached to minimize external API calls."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved current weather",
                    content = @Content(schema = @Schema(implementation = CurrentWeatherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid location coordinates"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit exceeded"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "External weather service unavailable"
            )
    })
    @GetMapping("/current")
    public ResponseEntity<CurrentWeatherResponse> getCurrentWeather(
            @Parameter(description = "Latitude coordinate (-90 to 90)", example = "40.7128")
            @RequestParam
            @NotNull(message = "Latitude is required")
            @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
            @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
            BigDecimal latitude,

            @Parameter(description = "Longitude coordinate (-180 to 180)", example = "-74.0060")
            @RequestParam
            @NotNull(message = "Longitude is required")
            @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
            @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
            BigDecimal longitude
    ) {
        log.info("Received request for current weather: lat={}, lon={}", latitude, longitude);
        CurrentWeatherResponse response = weatherService.getCurrentWeather(latitude, longitude);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get hourly forecast",
            description = "Retrieves hourly weather forecast for the next 24 hours. " +
                         "Data is cached to minimize external API calls."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved hourly forecast",
                    content = @Content(schema = @Schema(implementation = HourlyWeatherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid location coordinates"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit exceeded"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "External weather service unavailable"
            )
    })
    @GetMapping("/hourly")
    public ResponseEntity<HourlyWeatherResponse> getHourlyForecast(
            @Parameter(description = "Latitude coordinate (-90 to 90)", example = "40.7128")
            @RequestParam
            @NotNull(message = "Latitude is required")
            @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
            @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
            BigDecimal latitude,

            @Parameter(description = "Longitude coordinate (-180 to 180)", example = "-74.0060")
            @RequestParam
            @NotNull(message = "Longitude is required")
            @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
            @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
            BigDecimal longitude
    ) {
        log.info("Received request for hourly forecast: lat={}, lon={}", latitude, longitude);
        HourlyWeatherResponse response = weatherService.getHourlyForecast(latitude, longitude);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get daily forecast",
            description = "Retrieves daily weather forecast for the next 7 days. " +
                         "Data is cached to minimize external API calls."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved daily forecast",
                    content = @Content(schema = @Schema(implementation = DailyWeatherResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid location coordinates"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit exceeded"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "External weather service unavailable"
            )
    })
    @GetMapping("/daily")
    public ResponseEntity<DailyWeatherResponse> getDailyForecast(
            @Parameter(description = "Latitude coordinate (-90 to 90)", example = "40.7128")
            @RequestParam
            @NotNull(message = "Latitude is required")
            @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
            @DecimalMax(value = "90.0", message = "Latitude must be at most 90")
            BigDecimal latitude,

            @Parameter(description = "Longitude coordinate (-180 to 180)", example = "-74.0060")
            @RequestParam
            @NotNull(message = "Longitude is required")
            @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
            @DecimalMax(value = "180.0", message = "Longitude must be at most 180")
            BigDecimal longitude
    ) {
        log.info("Received request for daily forecast: lat={}, lon={}", latitude, longitude);
        DailyWeatherResponse response = weatherService.getDailyForecast(latitude, longitude);
        return ResponseEntity.ok(response);
    }
}
