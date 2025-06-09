package com.example.mcp.server.demo.prompts;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public interface BasePrompt {

    McpSchema.Prompt getPrompt();
    BiFunction<McpAsyncServerExchange, McpSchema.GetPromptRequest, Mono<McpSchema.GetPromptResult>> getAsyncPromptHandler();
}
