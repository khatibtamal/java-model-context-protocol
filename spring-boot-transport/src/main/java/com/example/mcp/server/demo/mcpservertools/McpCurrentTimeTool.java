package com.example.mcp.server.demo.mcpservertools;

import com.example.mcp.server.demo.service.CurrentTimeService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.converter.FormatProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.util.json.schema.JsonSchemaGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class McpCurrentTimeTool implements ToolCallback, FormatProvider,
        BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> {

    private final CurrentTimeService currentTimeService;

    public McpCurrentTimeTool(final CurrentTimeService currentTimeService) {
        this.currentTimeService = currentTimeService;
    }

    @Override
    public McpSchema.CallToolResult apply(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> stringObjectMap) {
        return new McpSchema.CallToolResult(call(null), false);
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition
                .builder()
                .name("CurrentDateTime")
                .description("Current Date and Time of Server")
                .inputSchema(getFormat())
                .build();
    }

    @Override
    public String call(String toolInput) {
        return currentTimeService.getCurrentTime();
    }

    @Override
    public String call(String toolInput, ToolContext toolContext) {
        return call(toolInput);
    }

    @Override
    public String getFormat() {
        Method method = ReflectionUtils.findMethod(CurrentTimeService.class, "getCurrentTime");
        assert method != null;
        return JsonSchemaGenerator.generateForMethodInput(method);
    }
}
