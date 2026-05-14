package com.feedbackmanagement.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationUtility {

    public void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put(fieldName, fieldName + " must not be null");
            throw new com.feedbackmanagement.exception.ValidationException("Validation failed", errors);
        }
    }

    public void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put(fieldName, fieldName + " must not be empty");
            throw new com.feedbackmanagement.exception.ValidationException("Validation failed", errors);
        }
    }

    public void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            Map<String, String> errors = new HashMap<>();
            errors.put(fieldName, fieldName + " must be between " + min + " and " + max);
            throw new com.feedbackmanagement.exception.ValidationException("Validation failed", errors);
        }
    }

    public void validateStringLength(String value, int minLength, int maxLength, String fieldName) {
        if (value != null && (value.length() < minLength || value.length() > maxLength)) {
            Map<String, String> errors = new HashMap<>();
            errors.put(fieldName, fieldName + " must be between " + minLength + " and " + maxLength + " characters");
            throw new com.feedbackmanagement.exception.ValidationException("Validation failed", errors);
        }
    }
}