package com.example.mcp.server.demo.prompts;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.function.BiFunction;

public interface BasePrompt {

    McpSchema.Prompt getPrompt();
    BiFunction<McpSyncServerExchange, McpSchema.GetPromptRequest, McpSchema.GetPromptResult> getPromptHandler();
}
