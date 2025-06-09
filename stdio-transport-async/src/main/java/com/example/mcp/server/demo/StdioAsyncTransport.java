package com.example.mcp.server.demo;

import com.example.mcp.server.demo.config.McpServerConfig;
import com.example.mcp.server.demo.prompts.DemoPrompt;
import com.example.mcp.server.demo.resources.ItemResource;
import com.example.mcp.server.demo.services.ItemService;
import com.example.mcp.server.demo.services.WeatherService;
import com.example.mcp.server.demo.tools.WeatherTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

public class StdioAsyncTransport {

    private static final WeatherService weatherService = new WeatherService();
    private static final ItemService itemService = new ItemService();
    private static final WeatherTool weatherTool = new WeatherTool(weatherService);
    private static final ItemResource itemResource = new ItemResource(itemService);
    private static final DemoPrompt demoPrompt = new DemoPrompt();
    private static final McpServerConfig mcpServerConfig = new McpServerConfig(weatherTool, demoPrompt, itemResource);

    public static void main(String[] args) {

        /**
         * Step 1: Choose the transport provider.
         */
        StdioServerTransportProvider stdioTransport = new StdioServerTransportProvider(new ObjectMapper());

        /**
         * Step 2: Build the MCP Server with the transport provider and capabilities.
         */
        McpAsyncServer mcpAsyncServer = McpServer.async(stdioTransport)
        .capabilities(McpSchema.ServerCapabilities.builder()
                .tools(true)
                .prompts(true)
                .resources(true, true)
                .build())
        .build();

        mcpServerConfig.addTools(mcpAsyncServer)
                .then(mcpServerConfig.addPrompts(mcpAsyncServer))
                .then(mcpServerConfig.addResources(mcpAsyncServer))
                .subscribe();
    }
}
