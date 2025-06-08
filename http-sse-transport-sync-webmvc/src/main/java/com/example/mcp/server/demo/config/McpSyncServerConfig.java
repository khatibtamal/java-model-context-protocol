package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.tools.WeatherTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

@Configuration
public class McpSyncServerConfig {

    private final WeatherTool weatherTool;
    private final DemoPrompt demoPrompt;
    private final ItemResource itemResource;

    public McpSyncServerConfig(final WeatherTool weatherTool, final DemoPrompt demoPrompt, final ItemResource itemResource) {
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

        /**
         * Step 2: Build the MCP Server with the transport provider.
         * During the build process, the server will keep reference to the transport provider
         * therefore we don't really need to keep a reference to of the MCP Server.
         *
         * The only reason we are keeping a reference to the MCP Server here is because
         * we will use the object to define the server capabilities and tools.
         */

        // This is an example of a bare bones server, with no capabilities.
//        McpSyncServer mcpSyncServer = McpServer.sync(transportProvider).build();

        // This is an example of a server with capabilities.
        McpSyncServer mcpSyncServer = McpServer.sync(transportProvider)
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .prompts(true)
                        .resources(true, true)
                        .build())
                .build();

        addTools(mcpSyncServer);
        addPrompts(mcpSyncServer);
        addResources(mcpSyncServer);

        /**
         * Step 4: Expose the router functions so that MCP Clients can connect
         */
        return transportProvider.getRouterFunction();
    }

    private void addTools(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncToolSpecification weatherSyncTool = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(weatherTool.getName(),
                        weatherTool.getDescription(),
                        weatherTool.inputJsonSchema()),
                weatherTool.getExecutionFunction()
        );

        mcpSyncServer.addTool(weatherSyncTool);
    }

    private void addPrompts(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncPromptSpecification promptSpec = new McpServerFeatures.SyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getPromptHandler()
        );

        mcpSyncServer.addPrompt(promptSpec);
    }

    private void addResources(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncResourceSpecification resourceSpecification = new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource(itemResource.uri(), itemResource.name(), itemResource.description(), itemResource.mimeType(), itemResource.annotations()),
                itemResource.resourceReadHandler()
        );
        mcpSyncServer.addResource(resourceSpecification);
    }
}