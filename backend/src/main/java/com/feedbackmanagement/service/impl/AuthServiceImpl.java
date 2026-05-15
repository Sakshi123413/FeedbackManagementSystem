package com.feedbackmanagement.service.impl;

import com.feedbackmanagement.dto.request.LoginRequest;
import com.feedbackmanagement.dto.request.SignupRequest;
import com.feedbackmanagement.dto.response.AuthResponse;
import com.feedbackmanagement.entity.Role;
import com.feedbackmanagement.entity.User;
import com.feedbackmanagement.exception.BadRequestException;
import com.feedbackmanagement.exception.UnauthorizedException;
import com.feedbackmanagement.repository.UserRepository;
import com.feedbackmanagement.security.CustomUserDetails;
import com.feedbackmanagement.security.JwtService;
import com.feedbackmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        log.info("Processing signup request for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup attempt with existing email: {}", request.getEmail());
            throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        CustomUserDetails userDetails = CustomUserDetails.fromUser(savedUser);
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole().name())
                .userId(savedUser.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Processing login request for email: {}", request.getEmail());

        // Step 1: Verify user exists in database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login attempt for non-existent email: {}", request.getEmail());
                    return new UnauthorizedException("Invalid email or password");
                });

        // Step 2: Authenticate using Spring Security (validates password via BCrypt)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            log.warn("Login failed for email: {} - {}", request.getEmail(), e.getMessage());
            throw new UnauthorizedException("Invalid email or password");
        }

        // Step 3: Generate token for the authenticated user
        CustomUserDetails userDetails = CustomUserDetails.fromUser(user);
        String token = jwtService.generateToken(userDetails);

        log.info("Login successful for user: {}", user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}