package com.tracker.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupPayload {
    private String username;
    private String email;
    private String password;
}
