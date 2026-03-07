package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Dtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
    }

    // ── Login Request ─────────────────────────────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    // ── OTP Verify Request ────────────────────────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OtpVerifyRequest {
        private String email;
        private String otp;
    }

    // ── Auth Response (JWT Token) ─────────────────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResponse {
        private String token;
        private String email;
        private String message;

        public AuthResponse(String token, String message) {
            this.token = token;
            this.message = message;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileRequest {
        private String name;
        private String email;
        private String password;
        private Boolean isAccountVerified;
    }

    // ── Profile Response ──────────────────────────────────────
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileResponse {
        private String name;
        private String email;
        private Boolean isAccountVerified;
    }
}
