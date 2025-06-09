package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.tools.WeatherTool;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

public class McpServerConfig {

    private final WeatherTool weatherTool;
    private final DemoPrompt demoPrompt;
    private final ItemResource itemResource;

    public McpServerConfig(final WeatherTool weatherTool, final DemoPrompt demoPrompt, final ItemResource itemResource) {
        this.weatherTool = weatherTool;
        this.demoPrompt = demoPrompt;
        this.itemResource = itemResource;
    }

    public Mono<Void> addTools(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncToolSpecification weatherAsyncTool = new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool(weatherTool.getName(),
                        weatherTool.getDescription(),
                        weatherTool.inputJsonSchema()),
                weatherTool.getAsyncExecutionFunction()
        );

        return mcpAsyncServer.addTool(weatherAsyncTool);
    }

    public Mono<Void> addPrompts(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncPromptSpecification promptSpec = new McpServerFeatures.AsyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getAsyncPromptHandler()
        );

        return mcpAsyncServer.addPrompt(promptSpec);
    }

    public Mono<Void> addResources(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncResourceSpecification resourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
                new McpSchema.Resource(itemResource.uri(), itemResource.name(), itemResource.description(), itemResource.mimeType(), itemResource.annotations()),
                itemResource.asyncResourceRead()
        );

        return mcpAsyncServer.addResource(resourceSpecification);
    }
}
