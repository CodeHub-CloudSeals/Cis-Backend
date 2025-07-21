package com.spring.implementation.model;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Constructors, getters, setters
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // builder‐style for convenience
    public ErrorResponse status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse error(String error) {
        this.error = error;
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponse path(String path) {
        this.path = path;
        return this;
    }
    
    // getters and setters omitted for brevity
}