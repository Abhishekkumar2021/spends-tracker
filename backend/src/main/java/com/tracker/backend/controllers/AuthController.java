package com.tracker.backend.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.backend.models.LoginPayload;
import com.tracker.backend.models.SignupPayload;
import com.tracker.backend.services.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public Mono<String> signup(@RequestBody SignupPayload signupPayload) {
        return authService.signup(signupPayload);
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody LoginPayload loginPayload) {
        return authService.login(loginPayload);
    }
    
}
