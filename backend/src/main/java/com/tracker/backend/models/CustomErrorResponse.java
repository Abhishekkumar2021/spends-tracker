package com.tracker.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse{
    private String message;
    private int statusCode;
}