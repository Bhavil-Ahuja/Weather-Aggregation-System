package com.weatherapp.exception;

public class InvalidLocationException extends WeatherServiceException {
    
    public InvalidLocationException(String message) {
        super(message);
    }
}
