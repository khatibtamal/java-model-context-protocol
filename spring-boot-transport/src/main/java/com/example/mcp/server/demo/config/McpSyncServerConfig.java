package com.example.mcp.server.demo.config;

import com.example.mcp.server.demo.mcpservertools.McpCurrentTimeTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpSyncServerConfig {

    private final McpCurrentTimeTool currentTimeCallback;

    public McpSyncServerConfig(final McpCurrentTimeTool currentTimeCallback) {
        this.currentTimeCallback = currentTimeCallback;
    }

    @Bean
    public McpSyncServer syncSpecification() {
        return McpServer.sync(transportProvider())
                .serverInfo("mcp-sse-sync-server", "0.0.1")
                .capabilities(serverCapabilities())
                .tools(syncToolSpecification())
                .build();
    }

    public McpServerFeatures.SyncToolSpecification syncToolSpecification() {
        McpSchema.Tool toolSpecification = new McpSchema.Tool(
                currentTimeCallback.getToolDefinition().name(),
                currentTimeCallback.getToolDefinition().description(),
                currentTimeCallback.getToolDefinition().inputSchema()
        );

        return new McpServerFeatures.SyncToolSpecification(toolSpecification, currentTimeCallback);
    }

    public McpSchema.ServerCapabilities serverCapabilities() {
        return McpSchema.ServerCapabilities.builder()
                .tools(true)
                .logging()
                .build();
    }

    public McpServerTransportProvider transportProvider() {
        return new HttpServletSseServerTransportProvider(new ObjectMapper(), "/mcp/message", "/sse");
    }
}