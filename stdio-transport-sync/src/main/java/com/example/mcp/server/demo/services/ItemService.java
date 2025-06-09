package com.example.mcp.server.demo.services;

import java.util.ArrayList;
import java.util.List;

public class ItemService {

    private int itemCount = 1;

    public List<String> getItems() throws InterruptedException {
        List<String> items = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            items.add(generateItems());
        }
        return items;
    }

    private String generateItems() throws InterruptedException {
        Thread.sleep(300L);
        return String.format("Item %d", itemCount++);
    }
}
