package com.example.mcp.server.demo.prompts;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.BiFunction;

public class DemoPrompt implements BasePrompt {

    @Override
    public McpSchema.Prompt getPrompt() {
        return new McpSchema.Prompt("DemoPrompt",
                "This is a demo prompt for the MCP server.", Collections.emptyList());
    }

    @Override
    public BiFunction<McpAsyncServerExchange, McpSchema.GetPromptRequest, Mono<McpSchema.GetPromptResult>> getAsyncPromptHandler() {
        return (exchange, request) -> Mono.just(new McpSchema.GetPromptResult("Demo Prompt Result",
                Collections.singletonList(new McpSchema.PromptMessage(McpSchema.Role.ASSISTANT,
                        new McpSchema.TextContent("Imagine the Demo Prompt was executed by an LLM and this is the response!!")))
        ));
    }
}
