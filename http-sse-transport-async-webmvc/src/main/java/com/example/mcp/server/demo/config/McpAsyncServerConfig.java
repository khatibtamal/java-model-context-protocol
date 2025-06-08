package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.tools.WeatherTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class McpAsyncServerConfig {

    private final WeatherTool weatherTool;
    private final DemoPrompt demoPrompt;
    private final ItemResource itemResource;

    public McpAsyncServerConfig(final WeatherTool weatherTool, final DemoPrompt demoPrompt, final ItemResource itemResource) {
        this.weatherTool = weatherTool;
        this.demoPrompt = demoPrompt;
        this.itemResource = itemResource;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctions() {
        /**
         * Step 1: Choose the transport provider.
         */
        WebMvcSseServerTransportProvider transportProvider = new WebMvcSseServerTransportProvider(
                new ObjectMapper(), "/mcp/message", "/sse");

        // This is an example of a bare bones server, with no capabilities.
//        McpAsyncServer mcpAsyncServer = McpServer.async(transportProvider).build();

        // This is an example of a server with capabilities.
        McpAsyncServer mcpAsyncServer = McpServer.async(transportProvider)
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .prompts(true)
                        .resources(true, true)
                        .build())
                .build();

        addTools(mcpAsyncServer);
        addPrompts(mcpAsyncServer);
        addResources(mcpAsyncServer);

        /**
         * Step 4: Expose the router functions so that MCP Clients can connect
         */
        return transportProvider.getRouterFunction();
    }

    private void addTools(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncToolSpecification weatherAsyncTool = new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool(weatherTool.getName(),
                        weatherTool.getDescription(),
                        weatherTool.inputJsonSchema()),
                weatherTool.getAsyncExecutionFunction()
        );

        mcpAsyncServer.addTool(weatherAsyncTool).block();
    }

    private void addPrompts(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncPromptSpecification promptSpec = new McpServerFeatures.AsyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getAsyncPromptHandler()
        );

        mcpAsyncServer.addPrompt(promptSpec).block();
    }

    private void addResources(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncResourceSpecification resourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
                new McpSchema.Resource(itemResource.uri(), itemResource.name(), itemResource.description(), itemResource.mimeType(), itemResource.annotations()),
                itemResource.asyncResourceRead()
        );

        mcpAsyncServer.addResource(resourceSpecification).block();
    }
}
