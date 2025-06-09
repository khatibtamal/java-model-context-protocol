package com.example.mcp.server.demo.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    public Flux<String> getItems() {
        return Flux.range(1, 5).flatMap(this::generateItems);
    }

    private Mono<String> generateItems(final int val) {
        return Mono.delay(Duration.ofMillis(300L)).then(Mono.defer(() -> Mono.just(String.format("Item %d", val))));
    }
}
