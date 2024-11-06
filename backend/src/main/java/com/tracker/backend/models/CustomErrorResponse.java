package com.tracker.backend.models;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse{
    private String message;
    private HttpStatus status;
}