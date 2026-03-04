package com.ayahathout.book_service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Test {
    @Value("${test-message}")
    private String msg;

    @PostConstruct
    public void init() {
        System.out.println("Test message: " + msg);
    }
}
