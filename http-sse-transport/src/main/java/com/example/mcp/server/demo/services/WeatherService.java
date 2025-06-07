package com.example.mcp.server.demo.services;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    public String getCurrentWeather(String city, String country) {
        return String.format("The current weather in %s, %s is sunny with a temperature of 25°C.", city, country);
    }
}
