package com.example.demo.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank( message = "New Password Required")
    private String newPassword;
    @NotBlank( message = "OTP Required")
    private String otp;
    @NotBlank( message = "Email Required")
    private String email;

}
