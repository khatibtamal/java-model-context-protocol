package com.example.mcp.server.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class McpSyncServerConfig {

    @Bean
    public RouterFunction<ServerResponse> mvcMcpRouterFunction() {
        WebMvcSseServerTransportProvider transportProvider = new WebMvcSseServerTransportProvider(new ObjectMapper(), "/mcp/message", "/sse");

        /**
         * The MCP Server build is what actually configures the transport provided endpoints.
         *
         * For detail on how McpServer configures the transportProvider it was provided, see the following
         * {@link McpServer#sync(McpServerTransportProvider)}
         * {@link McpServer.SyncSpecification#build()}
         * {@link io.modelcontextprotocol.server.McpAsyncServer#McpAsyncServer(McpServerTransportProvider, ObjectMapper, McpServerFeatures.Async)}
         * {@link io.modelcontextprotocol.server.McpAsyncServer.AsyncServerImpl#AsyncServerImpl(McpServerTransportProvider, ObjectMapper, McpServerFeatures.Async))}
         */
        McpServer.sync(transportProvider)
                .serverInfo("mcp-sse-sync-server", "0.0.1")
                .build();

        return transportProvider.getRouterFunction();
    }
}