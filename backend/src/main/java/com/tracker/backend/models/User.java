package com.tracker.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import org.springframework.data.annotation.Id;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String username;
    private String email;
    private String password;
    private Role role;
    private Boolean emailVerified;
    private String verificationOTP;
    private Date verificationOTPExpiryDate;
    public User() {
        this.emailVerified = false;
    }
}
