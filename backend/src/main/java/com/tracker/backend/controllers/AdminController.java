package com.tracker.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.backend.models.CustomResponse;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomResponse> getProfile() {
        CustomResponse response = new CustomResponse();
        response.setMessage("Admin profile retrieved successfully");
        response.setStatus(HttpStatus.OK);
        return Mono.just(response);
    }
}
