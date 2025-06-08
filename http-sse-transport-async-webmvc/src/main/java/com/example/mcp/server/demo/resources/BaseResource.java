package com.example.mcp.server.demo.resources;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public interface BaseResource {

    String uri();
    String name();
    String description();
    String mimeType();
    McpSchema.Annotations annotations();
    BiFunction<McpAsyncServerExchange, McpSchema.ReadResourceRequest, Mono<McpSchema.ReadResourceResult>> asyncResourceRead();
}