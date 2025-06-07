package com.example.mcp.server.demo.tools;

import com.example.mcp.server.demo.services.WeatherService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiFunction;

@Component
public class WeatherTool implements BaseTool {

    private final WeatherService weatherService;

    public WeatherTool(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public String getName() {
        return "WeatherTool";
    }

    @Override
    public String getDescription() {
        return "This tool provides the current weather information based on a given location in the form of a city and country.";
    }

    @Override
    public String inputJsonSchema() {
        return """
                {
                  "$schema": "http://json-schema.org/draft-07/schema#",
                  "type": "object",
                  "properties": {
                    "city": {
                      "type": "string"
                    },
                    "country": {
                      "type": "string"
                    }
                  },
                  "required": ["city", "country"]
                }
            """;
    }

    @Override
    public BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> getExecutionFunction() {
        return (exchange, input) -> {
            String city = (String) input.get("city");
            String country = (String) input.get("country");
            String weatherInfo = weatherService.getCurrentWeather(city, country);
            return new McpSchema.CallToolResult(weatherInfo, false);
        };
    }
}
