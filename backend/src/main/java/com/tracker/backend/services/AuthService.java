package com.tracker.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tracker.backend.exceptions.ServiceException;
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
    private final EmailService emailService;
    private final OTPService otpService;
    private final JWTUtil jwtUtil;

    private static final String INVALID_EMAIL = "Invalid email";

    @Value("${otp.expiration}")
    private long otpExpiration;

    public Mono<CustomResponse> signup(SignupPayload signupPayload) throws ServiceException {
        User user = new User();
        user.setUsername(signupPayload.getUsername());
        user.setEmail(signupPayload.getEmail());
        user.setPassword(passwordEncoder.encode(signupPayload.getPassword()));
        user.setRole(signupPayload.getIsAdmin() == Boolean.TRUE ? Role.ADMIN : Role.USER);
        // Check if already exists then return error
        return userRepository.existsByUsername(user.getUsername())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        throw new ServiceException("Username already exists", HttpStatus.CONFLICT);
                    }
                    return userRepository.existsByEmail(user.getEmail());
                })
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new ServiceException("Email already exists", HttpStatus.CONFLICT));
                    }
                    return userRepository.save(user);
                })
                .map(u -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("User signed up successfully");
                    response.addData("user", u);
                    response.setStatusCode(HttpStatus.CREATED.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("User signed up successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    public Mono<CustomResponse> login(LoginPayload loginPayload) {
        // Generate JWT token upon successful login
        final String username = loginPayload.getUsername();
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ServiceException("Invalid username", HttpStatus.UNAUTHORIZED)))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(loginPayload.getPassword(), user.getPassword())) {
                        return Mono.error(new ServiceException("Invalid password", HttpStatus.UNAUTHORIZED));
                    }
                    CustomResponse response = new CustomResponse();
                    response.setMessage("User logged in successfully");
                    response.addData("accessToken", jwtUtil.generateAccessToken(username, user.getRole()));
                    response.addData("refreshToken", jwtUtil.generateRefreshToken(username));
                    response.setStatusCode(HttpStatus.OK.value());
                    return Mono.just(response);
                })
                .doOnSuccess(success -> loggingService.info("User logged in successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Refresh JWT token
    public Mono<CustomResponse> refresh(String token) {
        String username = jwtUtil.extractUsernameFromRefreshToken(token);
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ServiceException("Invalid username", HttpStatus.UNAUTHORIZED)))
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(jwtUtil.validateRefreshToken(token))) {
                        return Mono.error(new ServiceException("Invalid refresh token", HttpStatus.UNAUTHORIZED));
                    }
                    CustomResponse response = new CustomResponse();
                    response.setMessage("Token refreshed successfully");
                    response.addData("accessToken", jwtUtil.generateAccessToken(username, user.getRole()));
                    response.setStatusCode(HttpStatus.OK.value());
                    return Mono.just(response);
                })
                .doOnSuccess(success -> loggingService.info("Token refreshed successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Forgot password reset request
    public Mono<CustomResponse> forgotPassword(String email) {
        return userRepository.existsByEmail(email)
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new ServiceException("Email not found", HttpStatus.NOT_FOUND));
                    }

                    String token = jwtUtil.generateOTPToken(email);

                    // send email with token
                    CustomResponse response = new CustomResponse();
                    response.setMessage(String.format("Password reset mail sent to %s", email));
                    response.addData("token", token);
                    response.setStatusCode(HttpStatus.OK.value());
                    return Mono.just(response);
                })
                .doOnSuccess(success -> loggingService.info("Password reset mail sent successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));

    }

    // Verify OTP and reset password
    public Mono<CustomResponse> resetPassword(String token, String newPassword) {
        String email = jwtUtil.extractEmailFromOTPToken(token);
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ServiceException(INVALID_EMAIL, HttpStatus.UNAUTHORIZED)))
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(jwtUtil.validateOTPToken(token))) {
                        return Mono.error(new ServiceException("Invalid OTP", HttpStatus.UNAUTHORIZED));
                    }
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(user);
                })
                .map(user -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("Password reset successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Password reset successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Send email verification OTP
    public Mono<CustomResponse> sendVerificationOTP(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ServiceException(INVALID_EMAIL, HttpStatus.BAD_REQUEST)))
                .flatMap(user -> {
                    String otp = otpService.generateOTP();
                    user.setVerificationOTP(otp);
                    user.setVerificationOTPExpiryDate(new Date(System.currentTimeMillis() + otpExpiration));
                    return userRepository.save(user);
                })
                .flatMap(user -> emailService.sendverificationEmail(email, user.getUsername(), user.getVerificationOTP()))
                .flatMap(response -> {
                    CustomResponse customResponse = new CustomResponse();
                    customResponse.setMessage("Email verification OTP sent successfully");
                    customResponse.setStatusCode(HttpStatus.OK.value());
                    return Mono.just(customResponse);
                })
                .doOnSuccess(success -> loggingService.info("Email verification OTP sent successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));

    }

    // Verify email OTP
    public Mono<CustomResponse> verifyEmailOTP(String email, String otp) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ServiceException(INVALID_EMAIL, HttpStatus.BAD_REQUEST)))
                .flatMap(user -> {
                    if (!otpService.validateOTP(otp, user.getVerificationOTPExpiryDate())) {
                        return Mono.error(new ServiceException("Invalid OTP", HttpStatus.UNAUTHORIZED));
                    }
                    user.setEmailVerified(true);
                    return userRepository.save(user);
                })
                .map(user -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("Email verified successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Email verified successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

}
