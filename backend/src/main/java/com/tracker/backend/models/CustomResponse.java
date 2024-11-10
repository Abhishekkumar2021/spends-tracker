package com.tracker.backend.models;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponse{
    private String message;
    private int statusCode;
    private Map<String, Object> data;

    public CustomResponse() {
        data = new HashMap<>();
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }
}