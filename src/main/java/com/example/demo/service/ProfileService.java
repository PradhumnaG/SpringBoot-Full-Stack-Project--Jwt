package com.example.demo.service;

import com.example.demo.io.ProfileRequest;
import com.example.demo.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String otp, String password);
    void sendOtp(String email);
    void verifyOtp(String email, String otp);
   /* String getLoggedInUserId(String email);*/
}
