# Weather Aggregation System

A Spring Boot application that fetches and caches weather data from OpenWeather API.

## Prerequisites

- Java 21
- Maven 3.6+
- OpenWeather API key (One Call API 3.0 subscription)

## Setup

1. Get your API key from [OpenWeather](https://openweathermap.org/api/one-call-3)
   - Subscribe to "One Call by Call" plan (free tier: 1,000 calls/day)

2. Configure the API key:
   - Copy `.env.example` to `.env` and add your API key
   - OR set environment variable: `export OPENWEATHER_API_KEY=your_api_key_here`
   - OR in IntelliJ: Run → Edit Configurations → Environment variables

3. Build and run:
```bash
mvn clean package -DskipTests
java -jar target/weather-service-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

```
GET /api/weather/current?latitude={lat}&longitude={lon}
GET /api/weather/hourly?latitude={lat}&longitude={lon}
GET /api/weather/daily?latitude={lat}&longitude={lon}
```

Example:
```
http://localhost:8080/api/weather/current?latitude=40.7128&longitude=-74.0060
```

## Health Check

```
http://localhost:8080/actuator/health
```

## Features

- Weather data caching (30 min TTL)
- Rate limiting (60 requests/min)
- Retry logic for external API calls
- Clean REST API with proper error handling
