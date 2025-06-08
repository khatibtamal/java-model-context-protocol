package com.example.mcp.server.demo.tools;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

public interface BaseTool {
    String getName();
    String getDescription();
    String inputJsonSchema();
    BiFunction<McpAsyncServerExchange, Map<String, Object>, Mono<McpSchema.CallToolResult>> getAsyncExecutionFunction();
}
