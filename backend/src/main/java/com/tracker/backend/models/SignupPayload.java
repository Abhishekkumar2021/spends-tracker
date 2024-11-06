package com.tracker.backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupPayload {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Is Admin cannot be blank")
    private Boolean isAdmin;
}
