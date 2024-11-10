package com.tracker.backend.services;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private static final int OTP_LENGTH = 6;
    private final Random random;

    public OTPService() {
        random = new Random();
    }

    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean validateOTP(String otp, Date expiryDate) {
        return otp.matches("[0-9]{" + OTP_LENGTH + "}") && new Date(System.currentTimeMillis()).before(expiryDate);
    }
}
