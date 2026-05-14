package com.feedbackmanagement.controller;

import com.feedbackmanagement.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health check APIs")
public class HealthCheckController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns the health status of the application")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthData = Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Feedback Management System",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", healthData));
    }
}