package com.example.mcp.server.demo.resources;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.function.BiFunction;

public interface BaseResource {

    String uri();
    String name();
    String description();
    String mimeType();
    McpSchema.Annotations annotations();
    BiFunction<McpSyncServerExchange, McpSchema.ReadResourceRequest, McpSchema.ReadResourceResult> resourceReadHandler();
}