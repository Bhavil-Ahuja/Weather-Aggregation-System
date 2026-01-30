package com.weatherapp.client;

import com.weatherapp.dto.openweather.OpenWeatherResponse;
import com.weatherapp.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
public class OpenWeatherClient {

    private final WebClient webClient;
    private final String apiKey;

    public OpenWeatherClient(
            WebClient weatherApiWebClient,
            @Value("${weather.api.api-key}") String apiKey) {
        this.webClient = weatherApiWebClient;
        this.apiKey = apiKey;
    }

    @Retryable(
            retryFor = {ExternalApiException.class},
            maxAttemptsExpression = "${weather.api.max-retries}",
            backoff = @Backoff(delayExpression = "${weather.api.retry-delay-ms}")
    )
    public OpenWeatherResponse fetchWeatherData(BigDecimal latitude, BigDecimal longitude) {
        log.info("Fetching weather data from OpenWeather API for location: ({}, {})", 
                latitude, longitude);

        try {
            OpenWeatherResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/onecall")
                            .queryParam("lat", latitude)
                            .queryParam("lon", longitude)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("exclude", "minutely,alerts")
                            .build())
                    .retrieve()
                    .onStatus(
                            status -> status.value() == HttpStatus.UNAUTHORIZED.value(),
                            clientResponse -> Mono.error(new ExternalApiException(
                                    "Invalid API key", 
                                    HttpStatus.UNAUTHORIZED.value()
                            ))
                    )
                    .onStatus(
                            status -> status.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ExternalApiException(
                                    "Location not found", 
                                    HttpStatus.NOT_FOUND.value()
                            ))
                    )
                    .onStatus(
                            status -> status.is4xxClientError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new ExternalApiException(
                                            "Client error: " + body, 
                                            clientResponse.statusCode().value()
                                    )))
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            clientResponse -> Mono.error(new ExternalApiException(
                                    "OpenWeather API server error", 
                                    clientResponse.statusCode().value()
                            ))
                    )
                    .bodyToMono(OpenWeatherResponse.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException(
                        "Empty response from OpenWeather API", 
                        HttpStatus.NO_CONTENT.value()
                );
            }

            log.info("Successfully fetched weather data from external API");
            return response;

        } catch (WebClientResponseException e) {
            log.error("WebClient error: status={}, body={}", 
                     e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ExternalApiException(
                    "Failed to fetch weather data: " + e.getMessage(), 
                    e.getStatusCode().value(), 
                    e
            );
        } catch (Exception e) {
            log.error("Unexpected error calling OpenWeather API", e);
            throw new ExternalApiException(
                    "Unexpected error: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    e
            );
        }
    }
}
