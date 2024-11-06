package com.tracker.backend.models;

public enum Role {
    USER,
    ADMIN;
    
    public String getName() {
        return name();
    }
}
