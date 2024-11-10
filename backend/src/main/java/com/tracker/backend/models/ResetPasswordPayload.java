package com.tracker.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordPayload {
    private String token;
    private String password;
}
