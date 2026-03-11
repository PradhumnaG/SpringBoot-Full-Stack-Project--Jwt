package com.example.demo.controller;

import com.example.demo.io.ProfileRequest;
import com.example.demo.io.ProfileResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
/*@RequestMapping("/api/v1.0")*/
@RequiredArgsConstructor
@Tag(name = "Profile", description = "User profile management endpoints")
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user profile", description = "Creates a new user profile and sends welcome email")
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        emailService.sendWelcomeEmail(response.getEmail(), response.getName());
        return response;
    }
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the authenticated user's profile information")
    @SecurityRequirement(name = "Bearer Authentication")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication.name") String email) {
         return profileService.getProfile(email);
    }


}
