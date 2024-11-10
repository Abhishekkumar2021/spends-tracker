package com.tracker.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailPayload {
    private String email;
    private String otp;
}
