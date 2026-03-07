package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.io.ProfileRequest;
import com.example.demo.io.ProfileResponse;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Override
    public void  resetPassword(String email,String otp, String password){
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found for the email: " + email));
        if(  existingUser.getResetOTP() == null || !existingUser.getResetOTP().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        if(existingUser.getResetOTPExpiredAt().before(new Date())){
            throw new RuntimeException("OTP expired");
        }
        existingUser.setPassword(passwordEncoder.encode(password));
        existingUser.setResetOTP(null);
        existingUser.setResetOTPExpiredAt(null);
//save to database
        userRepository.save(existingUser);
        try{
            emailService.sendOtpEmail(existingUser.getEmail(),otp);
            }catch (Exception e){
            throw new RuntimeException("Unable to send email");
        }


    }
    @Override
    public void sendOtp(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.isAccountVerified()) {
            return; // Already verified
        }

        // Generate 6-digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextLong(100000, 1000000));

        // Set Expiry to 24 hours [02:26:13]
        user.setVerifyOTP(otp);
        user.setVerifyOTPExpiredAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        userRepository.save(user);

        // Trigger Email [02:33:51]
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile = convertToUserEntity(request);
        // Logic exactly as shown in Screenshot 2
        if (!userRepository.existsByEmail(request.getEmail())) {
            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .verifyOTPExpiredAt(null)
                .verifyOTP(null)
                .resetOTP(null)
                .resetOTPExpiredAt(null)
                .build();
    }

    @Override
    public ProfileResponse getProfile(String email){
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found for the email: " + email));
        return convertToProfileResponse(existingUser);

    }
    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {

        return ProfileResponse.builder()
                .userID(newProfile.getUserId())
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .isAccountVerified(newProfile.isAccountVerified())
                .build();

    }
    @Override
    public void sendResetOtp(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Generate 6-digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextLong(100000, 1000000));

        // Set Expiry to 15 minutes [01:56:12]
        user.setResetOTP(otp);
        user.setResetOTPExpiredAt(new Date(System.currentTimeMillis() + (15 * 60 * 1000)));

        userRepository.save(user);

        // Send Email [01:58:38]
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.isAccountVerified()) {
            return; // Already verified
        }

        if (user.getVerifyOTP() == null || !user.getVerifyOTP().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getVerifyOTPExpiredAt().before(new Date())) {
            throw new RuntimeException("OTP expired");
        }

        user.setAccountVerified(true);
        user.setVerifyOTP(null);
        user.setVerifyOTPExpiredAt(null);

        userRepository.save(user);
    }

  /*  @Override
    public String getLoggedInUserId(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getUserId();
    }*/
}
