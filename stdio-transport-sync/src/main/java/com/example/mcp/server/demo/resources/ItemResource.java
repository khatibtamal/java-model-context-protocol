package com.example.mcp.server.demo.resources;

import com.example.mcp.server.demo.services.ItemService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
    public BiFunction<McpSyncServerExchange, McpSchema.ReadResourceRequest, McpSchema.ReadResourceResult> resourceReadHandler() {
        return (exchange, input) -> {
            List<McpSchema.ResourceContents> result = new ArrayList<>();
            try {
                itemService.getItems().forEach(item -> {
                    McpSchema.TextResourceContents textContent = new McpSchema.TextResourceContents(uri(), mimeType(), item);
                    result.add(textContent);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return new McpSchema.ReadResourceResult(result);
        };
    }
}
