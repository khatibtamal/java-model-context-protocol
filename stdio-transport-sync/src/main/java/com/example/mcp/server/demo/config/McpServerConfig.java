package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.tools.WeatherTool;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;

public class McpServerConfig {

    private final WeatherTool weatherTool;
    private final DemoPrompt demoPrompt;
    private final ItemResource itemResource;

    public McpServerConfig(final WeatherTool weatherTool, final DemoPrompt demoPrompt, final ItemResource itemResource) {
        this.weatherTool = weatherTool;
        this.demoPrompt = demoPrompt;
        this.itemResource = itemResource;
    }

    public void addTools(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncToolSpecification weatherSyncTool = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(weatherTool.getName(),
                        weatherTool.getDescription(),
                        weatherTool.inputJsonSchema()),
                weatherTool.getExecutionFunction()
        );

        mcpSyncServer.addTool(weatherSyncTool);
    }

    public void addPrompts(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncPromptSpecification promptSpec = new McpServerFeatures.SyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getPromptHandler()
        );

        mcpSyncServer.addPrompt(promptSpec);
    }

    public void addResources(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncResourceSpecification resourceSpecification = new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource(itemResource.uri(), itemResource.name(), itemResource.description(), itemResource.mimeType(), itemResource.annotations()),
                itemResource.resourceReadHandler()
        );
        mcpSyncServer.addResource(resourceSpecification);
    }
}
