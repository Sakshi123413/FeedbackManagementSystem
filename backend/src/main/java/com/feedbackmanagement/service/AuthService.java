package com.feedbackmanagement.service;

import com.feedbackmanagement.dto.request.LoginRequest;
import com.feedbackmanagement.dto.request.SignupRequest;
import com.feedbackmanagement.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}