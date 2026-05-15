package com.feedbackmanagement.controller;

import com.feedbackmanagement.entity.User;
import com.feedbackmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * TEMPORARY DEBUG endpoint - REMOVE IN PRODUCTION
 * Helps diagnose and fix password issues
 */
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Check if a user's password is properly encoded
     */
    @GetMapping("/check-password/{email}")
    public ResponseEntity<Map<String, Object>> checkPassword(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("exists", false);
            response.put("message", "User not found");
            return ResponseEntity.ok(response);
        }

        User user = userOpt.get();
        String storedPassword = user.getPassword();
        
        response.put("exists", true);
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRole().name());
        response.put("passwordHashStarts", storedPassword.substring(0, Math.min(30, storedPassword.length())));
        response.put("isBcryptFormat", storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$"));
        
        // Test with common password
        String testPassword = "password123";
        boolean matches = passwordEncoder.matches(testPassword, storedPassword);
        response.put("matches_password123", matches);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Reset user password to "password123" (TEMPORARY)
     */
    @PostMapping("/reset-password/{email}")
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.ok(response);
        }

        User user = userOpt.get();
        String newPassword = "password123";
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "Password reset to 'password123'");
        response.put("email", email);
        
        log.warn("DEBUG: Password reset for email: {}", email);
        
        return ResponseEntity.ok(response);
    }
}
