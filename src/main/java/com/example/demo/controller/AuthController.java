package com.example.demo.controller;


import com.example.demo.io.AuthRequest;
import com.example.demo.io.AuthResponse;
import com.example.demo.io.ResetPasswordRequest;
import com.example.demo.service.AppUserDetailsService;
import com.example.demo.service.ProfileService;
import com.example.demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Authentication", description = "Authentication endpoints for user login, registration, and password reset")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtils jwtUtils;
    private final ProfileService profileService;


    // ── POST /register ─────────────────────────────────────────────────────────
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with email, password, and name")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            String name = body.get("name");

            if (email == null || password == null || name == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields: email, password, name"));
            }

            return ResponseEntity.ok(Map.of("message", "User registered successfully", "email", email, "name", name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── POST /login ────────────────────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user with email and password, returns JWT token")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticate(request.getEmail(), request.getPassword());

            // Load user details and generate token
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
            final String jwt = jwtUtils.generateToken(userDetails);

            // Create the ResponseCookie
            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(false) // Developer typically sets false for localhost, true for prod
                    .path("/")
                    .maxAge(24 * 60 * 60) // 1 day in seconds, or Duration.ofDays(1)
                    .sameSite("Strict")
                    .build();

            // Return response with Cookie header and Body
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(request.getEmail(), jwt));
        } catch (BadCredentialsException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } catch (DisabledException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }

    /*    // Logic up to 56:20 - Fetching UserDetails before generating JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // The developer stops here to create the JwtUtil class
        return ResponseEntity.ok(userDetails); // Placeholder return until JWT logic is added*/
    }

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("/is-authenticated")
    @Operation(summary = "Check authentication status", description = "Verifies if the current user is authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication.name") String email) {

        return ResponseEntity.ok(email != null);

}
/*@PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try{
            profileService
        }
}*/
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets user password using OTP verification")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        try{
            profileService.resetPassword(request.getEmail(),request.getOtp(),request.getNewPassword());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());

        }
    }
    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP", description = "Sends OTP to the authenticated user's email for verification")
    @SecurityRequirement(name = "Bearer Authentication")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication.name") String email){
        try{
            profileService.sendOtp(email);
        }
        catch (Exception e){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
    @PostMapping("verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP sent to user's email")
    @SecurityRequirement(name = "Bearer Authentication")
    public void verifyEmail(@RequestBody Map<String,Object> request,@CurrentSecurityContext(expression = "authentication.name") String email){
        if (request.get("otp").toString() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP is required");
        }
        try{
            profileService.verifyOtp(email,request.get("otp").toString() );
        }catch (Exception e){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }

}
