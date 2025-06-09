package com.example.mcp.server.demo.resources;

import com.example.mcp.server.demo.services.ItemService;
import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ItemResource implements BaseResource {

    private final ItemService itemService;

    public ItemResource(final ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public String uri() {
        return "/items/all";
    }

    @Override
    public String name() {
        return "AllItemsResource";
    }

    @Override
    public String description() {
        return "Fetches all items from the inventory.";
    }

    @Override
    public String mimeType() {
        return "unknown";
    }

    @Override
    public McpSchema.Annotations annotations() {
        return new McpSchema.Annotations(new ArrayList<>(), -1.0);
    }

    @Override
    public BiFunction<McpAsyncServerExchange, McpSchema.ReadResourceRequest, Mono<McpSchema.ReadResourceResult>> asyncResourceRead() {
        return (exchange, input) -> Mono.defer(() -> itemService.getItems()
                .map((str) -> (McpSchema.ResourceContents) new McpSchema.TextResourceContents(uri(), mimeType(), str))
                .collect(Collectors.toList())
                .map(McpSchema.ReadResourceResult::new));
    }
}
