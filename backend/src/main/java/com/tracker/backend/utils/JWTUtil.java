package com.tracker.backend.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tracker.backend.models.Role;
import com.tracker.backend.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class JWTUtil {
    private final UserRepository userRepository;
    @Value("${jwt.secret.access}")
    private String accessTokenSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshTokenSecret;

    @Value("${jwt.secret.otp}")
    private String otpSecret;

    @Value("${jwt.expiration.secret}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpiration;

    @Value("${jwt.expiration.otp}")
    private long otpExpiration;
    
    // Methods
    public String generateAccessToken(String username, Role role) {
        return Jwts
                .builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey(accessTokenSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSignInKey(refreshTokenSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateOTPToken(String email) {
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + otpExpiration))
                .signWith(getSignInKey(otpSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateAccessToken(String token, String username) {
        String usernameFromToken = extractUsernameFromAccessToken(token);
        boolean isTokenExpired = isTokenExpired(token, accessTokenSecret);
        return usernameFromToken.equals(username) && !isTokenExpired;
    }

    public Boolean validateRefreshToken(String token) {
        return !isTokenExpired(token, refreshTokenSecret);
    }

    public Boolean validateOTPToken(String token) {
        return !isTokenExpired(token, otpSecret);
    }

    public String extractUsernameFromAccessToken(String token) {
        return getClaims(token, accessTokenSecret).getSubject();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return getClaims(token, refreshTokenSecret).getSubject();
    }

    public String extractEmailFromOTPToken(String token) {
        return getClaims(token, otpSecret).getSubject();
    }

    public Role extractRoleFromAccessToken(String token) {
        return (Role) getClaims(token, accessTokenSecret).get("role");
    }

    private Boolean isTokenExpired(String token, String secret) {
        Date expirationDate = getClaims(token, secret).getExpiration();
        return expirationDate.before(new Date());
    }

    private Key getSignInKey(String secret) {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token, String secret) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
