package com.tracker.backend.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayload {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
