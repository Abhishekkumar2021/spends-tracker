package com.tracker.backend.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tracker.backend.exceptions.ServiceException;
import com.tracker.backend.models.CustomResponse;
import com.tracker.backend.models.LoginPayload;
import com.tracker.backend.models.ResetPasswordPayload;
import com.tracker.backend.models.SignupPayload;
import com.tracker.backend.models.VerifyEmailPayload;
import com.tracker.backend.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Mono<CustomResponse> signup(@RequestBody @Valid SignupPayload signupPayload) throws ServiceException {
        return authService.signup(signupPayload);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Mono<CustomResponse> login(@RequestBody @Valid LoginPayload loginPayload) throws ServiceException {
        return authService.login(loginPayload);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public Mono<CustomResponse> refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forgot-password")
    public Mono<CustomResponse> forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reset-password")
    public Mono<CustomResponse> resetPassword(@RequestBody @Valid ResetPasswordPayload resetPasswordPayload) {
        return authService.resetPassword(resetPasswordPayload.getToken(), resetPasswordPayload.getPassword());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-verification-email")
    public Mono<CustomResponse> sendVerificationEmail(@RequestParam String email) {
        return authService.sendVerificationOTP(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify-email")
    public Mono<CustomResponse> verifyEmail(@RequestBody VerifyEmailPayload verifyEmailPayload) {
        return authService.verifyEmailOTP(verifyEmailPayload.getEmail(), verifyEmailPayload.getOtp());
    }
}
