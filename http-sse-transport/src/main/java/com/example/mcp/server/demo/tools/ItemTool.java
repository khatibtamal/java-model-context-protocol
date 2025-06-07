package com.example.mcp.server.demo.tools;

import com.example.mcp.server.demo.services.ItemService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class ItemTool implements BaseTool {

    private final ItemService itemService;

    public ItemTool(final ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public String getName() {
        return "ItemsTool";
    }

    @Override
    public String getDescription() {
        return "This tool provides a list of items available in the store.";
    }

    @Override
    public String inputJsonSchema() {
        return """
                {
                  "$schema": "http://json-schema.org/draft-07/schema#",
                  "type": "object",
                  "properties": {},
                  "required": []
                }
            """;
    }

    @Override
    public BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> getExecutionFunction() {
        return (exchange, input) -> {
            List<McpSchema.Content> result = new ArrayList<>();
            boolean error = false;
            try {
                itemService.getItems().forEach(item -> {
                    McpSchema.TextContent textContent = new McpSchema.TextContent(item);
                    result.add(textContent);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
                error = true;
            }

            return new McpSchema.CallToolResult(result, error);
        };
    }
}
