package com.example.mcp.server.demo.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ItemService {

    public Flux<String> getItems() {
        return Flux.range(1, 5).flatMap(this::generateItems);
    }

    private Mono<String> generateItems(final int val) {
        Mono.delay(Duration.ofMillis(300L)).block();
        return Mono.defer(() -> Mono.just(String.format("Item %d", val)));
    }
}