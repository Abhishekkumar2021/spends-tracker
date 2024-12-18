package com.tracker.backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailPayload {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String otp;
}
