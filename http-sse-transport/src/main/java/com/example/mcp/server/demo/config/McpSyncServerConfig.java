package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.tools.ItemTool;
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
    private final ItemTool itemTool;
    private final DemoPrompt demoPrompt;

    public McpSyncServerConfig(final WeatherTool weatherTool, final ItemTool itemTool, final DemoPrompt demoPrompt) {
        this.weatherTool = weatherTool;
        this.itemTool = itemTool;
        this.demoPrompt = demoPrompt;
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
                        .build())
                .build();

        addTools(mcpSyncServer);
        addPrompts(mcpSyncServer);

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

        McpServerFeatures.SyncToolSpecification itemSyncTool = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(itemTool.getName(),
                        itemTool.getDescription(),
                        itemTool.inputJsonSchema()),
                itemTool.getExecutionFunction()
        );

        mcpSyncServer.addTool(weatherSyncTool);
        mcpSyncServer.addTool(itemSyncTool);
    }

    private void addPrompts(McpSyncServer mcpSyncServer) {
        McpServerFeatures.SyncPromptSpecification promptSpec = new McpServerFeatures.SyncPromptSpecification(
                demoPrompt.getPrompt(), demoPrompt.getPromptHandler()
        );

        mcpSyncServer.addPrompt(promptSpec);
    }
}