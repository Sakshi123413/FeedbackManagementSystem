package com.feedbackmanagement.controller;

import com.feedbackmanagement.dto.request.LoginRequest;
import com.feedbackmanagement.dto.request.SignupRequest;
import com.feedbackmanagement.dto.response.ApiResponse;
import com.feedbackmanagement.dto.response.AuthResponse;
import com.feedbackmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse authResponse = authService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", authResponse));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user credentials and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity
                .ok(ApiResponse.success("Login successful", authResponse));
    }
}