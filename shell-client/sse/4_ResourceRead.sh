curl --location "localhost:8080/mcp/message?sessionId=$1" \
--header 'Accept-Charset: text/event-stream' \
--header 'Accept-Language: *' \
--header 'sec-fetch-mode: cors' \
--header 'accept-encoding: gzip, deflate' \
--header 'Content-Type: application/json' \
--data '{
          "jsonrpc" : "2.0",
          "id" : -1,
          "method" : "resources/read",
          "params" : {
            "uri" : "/items/all"
          }
        }'