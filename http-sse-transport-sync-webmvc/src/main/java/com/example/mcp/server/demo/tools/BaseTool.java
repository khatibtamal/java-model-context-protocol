package com.example.mcp.server.demo.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;
import java.util.function.BiFunction;

public interface BaseTool {
    String getName();
    String getDescription();
    String inputJsonSchema();
    BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> getExecutionFunction();
}
