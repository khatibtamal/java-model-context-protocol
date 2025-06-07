package com.example.mcp.server.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

@Configuration
public class McpSyncServerConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunctions() {
        WebMvcSseServerTransportProvider transportProvider = new WebMvcSseServerTransportProvider(
                new ObjectMapper(), "/mcp/message", "/sse");

        McpServer.sync(transportProvider).build();

        return transportProvider.getRouterFunction();
    }
}