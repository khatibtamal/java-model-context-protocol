# Model Context Protocol

## Server
An MCP server in the context of Java is a specification that we can build using the
java SDK. **This server is still a specification and needs a server like Tomcat or Jetty to run.**

The server consists of the following components:
- **Transport Layer**: In this project I am using [Http with SSE](https://modelcontextprotocol.io/docs/concepts/architecture#transport-layer)
- **Capabilities**: The server must define its capabilities in regards to specifications of [resources](https://modelcontextprotocol.io/docs/concepts/resources),
 [prompts](https://modelcontextprotocol.io/docs/concepts/prompts), [tools](https://modelcontextprotocol.io/docs/concepts/tools), 
 and [sampling](https://modelcontextprotocol.io/docs/concepts/sampling)

In a pure Java implementation we will create a McpSyncServer or McpAsyncServer object
and run that object on the actual server like Tomcat or Jetty.
But in this project because we are using Spring Boot, we can just expose that object as
a Spring Bean and let Spring Boot take care of the rest.

## Server components
- **Tool**: For a tool we will need to provide the following 4 components
  - Name: Name of the tool
  - Description: Description of the tool
  - Callback: The actual callback of the tool which is realized in MCP SDK using Java's BiFunction interface.
  - Schema: The schema that the MCP client will use to actually execute the callback.