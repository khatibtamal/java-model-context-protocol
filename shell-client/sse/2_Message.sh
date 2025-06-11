curl --location "localhost:8080/mcp/message?sessionId=$1" \
--header 'Accept-Charset: text/event-stream' \
--header 'Accept-Language: *' \
--header 'sec-fetch-mode: cors' \
--header 'accept-encoding: gzip, deflate' \
--header 'Content-Type: application/json' \
--data '{
  "jsonrpc" : "2.0",
  "id" : 0,
  "method" : "initialize",
  "params" : {
    "protocolVersion" : "2025-03-26",
    "capabilities" : {
      "sampling" : { },
      "roots" : {
        "listChanged" : true
      }
    },
    "clientInfo" : {
      "name" : "local-shell-client",
      "version" : "0.0.1"
    }
  }
}'