package com.feedbackmanagement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test password from schema.sql
        String storedHash = "$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu";
        String testPassword = "password123";
        
        System.out.println("Testing BCrypt password matching...");
        System.out.println("Stored hash: " + storedHash);
        System.out.println("Test password: " + testPassword);
        System.out.println("Matches: " + encoder.matches(testPassword, storedHash));
        
        // Generate new hash for comparison
        String newHash = encoder.encode(testPassword);
        System.out.println("\nNew hash generated: " + newHash);
        System.out.println("New hash matches: " + encoder.matches(testPassword, newHash));
    }
}
