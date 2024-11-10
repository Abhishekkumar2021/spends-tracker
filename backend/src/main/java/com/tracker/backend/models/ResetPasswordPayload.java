package com.tracker.backend.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordPayload {
    @NotBlank
    private String token;

    @NotBlank
    private String password;
}
