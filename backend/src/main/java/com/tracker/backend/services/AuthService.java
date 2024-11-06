package com.tracker.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tracker.backend.models.CustomResponse;
import com.tracker.backend.models.LoginPayload;
import com.tracker.backend.models.Role;
import com.tracker.backend.models.SignupPayload;
import com.tracker.backend.models.User;
import com.tracker.backend.repositories.UserRepository;
import com.tracker.backend.utils.JWTUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LoggingService loggingService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil JWTUtil;

    public Mono<CustomResponse> signup(SignupPayload signupPayload) {
        User user = new User();
        user.setUsername(signupPayload.getUsername());
        user.setEmail(signupPayload.getEmail());
        user.setPassword(passwordEncoder.encode(signupPayload.getPassword()));
        user.setRole(signupPayload.getIsAdmin() == Boolean.TRUE ? Role.ADMIN : Role.USER);
        user.setEmailVerified(false);
        return userRepository.save(user)
                .map(u -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("User signed up successfully");
                    response.addData("user", u);
                    response.setStatus(HttpStatus.CREATED);
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("User signed up successfully"))
                .doOnError(error -> loggingService.error("Error signing up user", error));
    }

    public Mono<CustomResponse> login(LoginPayload loginPayload) {
        // Generate JWT token upon successful login
        final String username = loginPayload.getUsername();
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(loginPayload.getPassword(), user.getPassword()))
                .map(user -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("User logged in successfully");
                    response.addData("token", JWTUtil.generateToken(username, user.getRole()));
                    response.setStatus(HttpStatus.OK);
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("User logged in successfully"))
                .doOnError(error -> loggingService.error("Error logging in user", error));
    }
}
