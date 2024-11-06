package com.tracker.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "users")
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private Role roles;
    private boolean isEmailVerified;
}
