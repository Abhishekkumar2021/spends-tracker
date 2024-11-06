package com.tracker.backend.services;

import org.springframework.stereotype.Service;

import com.tracker.backend.models.LoginPayload;
import com.tracker.backend.models.SignupPayload;
import com.tracker.backend.models.User;
import com.tracker.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LoggingService loggingService;
    private final UserRepository userRepository;

    public Mono<String> signup(SignupPayload signupPayload) {
        loggingService.info("User " + signupPayload.getUsername() + " signed up");
        User user = new User();
        user.setUsername(signupPayload.getUsername());
        user.setPassword(signupPayload.getPassword());
        user.setEmail(signupPayload.getEmail());
        user.setEmailVerified(false);
        return userRepository.save(user).map(u -> "User signed up successfully");
    }

    public Mono<String> login(LoginPayload loginPayload) {
        loggingService.info("User " + loginPayload.getUsername() + " logged in");
        return userRepository.findByUsername(loginPayload.getUsername()).map(user -> {
            if (user.getPassword().equals(loginPayload.getPassword())) {
                return "User logged in successfully";
            } else {
                return "Invalid credentials";
            }
        });
    }
}
