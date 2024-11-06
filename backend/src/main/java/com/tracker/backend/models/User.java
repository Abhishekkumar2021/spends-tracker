package com.tracker.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Indexed(unique = true, name = "unique_username")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Indexed(unique = true, name = "unique_email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Role role;
    private Boolean emailVerified;
}
