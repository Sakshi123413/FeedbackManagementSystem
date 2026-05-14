package com.feedbackmanagement.service.impl;

import com.feedbackmanagement.dto.request.LoginRequest;
import com.feedbackmanagement.dto.request.SignupRequest;
import com.feedbackmanagement.dto.response.AuthResponse;
import com.feedbackmanagement.entity.Role;
import com.feedbackmanagement.entity.User;
import com.feedbackmanagement.exception.BadRequestException;
import com.feedbackmanagement.repository.UserRepository;
import com.feedbackmanagement.security.CustomUserDetails;
import com.feedbackmanagement.security.JwtService;
import com.feedbackmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        log.info("Login successful for user: {}", userDetails.getEmail());
        return AuthResponse.builder()
                .token(token)
                .email(userDetails.getEmail())
                .name(userDetails.getName())
                .role(userDetails.getRole().name())
                .userId(userDetails.getId())
                .build();
    }
}