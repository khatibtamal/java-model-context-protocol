package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.tools.WeatherTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

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
    public RouterFunction<?> routerFunctions() {
        /**
         * Step 1: Choose the transport provider.
         */
        WebFluxSseServerTransportProvider transportProvider = new WebFluxSseServerTransportProvider(
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

        addTools(mcpAsyncServer)
                .then(addPrompts(mcpAsyncServer))
                .then(addResources(mcpAsyncServer))
                .subscribe();

        /**
         * Step 4: Expose the router functions so that MCP Clients can connect
         */
        return transportProvider.getRouterFunction();
    }

    private Mono<Void> addTools(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncToolSpecification weatherAsyncTool = new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool(weatherTool.getName(),
                        weatherTool.getDescription(),
                        weatherTool.inputJsonSchema()),
                weatherTool.getAsyncExecutionFunction()
        );

        return mcpAsyncServer.addTool(weatherAsyncTool);
    }

    private Mono<Void> addPrompts(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncPromptSpecification promptSpec = new McpServerFeatures.AsyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getAsyncPromptHandler()
        );

        return mcpAsyncServer.addPrompt(promptSpec);
    }

    private Mono<Void> addResources(McpAsyncServer mcpAsyncServer) {
        McpServerFeatures.AsyncResourceSpecification resourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
                new McpSchema.Resource(itemResource.uri(), itemResource.name(), itemResource.description(), itemResource.mimeType(), itemResource.annotations()),
                itemResource.asyncResourceRead()
        );

        return mcpAsyncServer.addResource(resourceSpecification);
    }
}
