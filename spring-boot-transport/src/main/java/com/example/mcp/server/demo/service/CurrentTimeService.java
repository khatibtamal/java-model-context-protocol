package com.example.mcp.server.demo.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CurrentTimeService {

    public String getCurrentTime() {
        return new Date().toString();
    }
}