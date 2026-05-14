package com.feedbackmanagement.controller;

import com.feedbackmanagement.dto.request.FeedbackRequest;
import com.feedbackmanagement.dto.request.FeedbackUpdateRequest;
import com.feedbackmanagement.dto.response.ApiResponse;
import com.feedbackmanagement.dto.response.FeedbackResponse;
import com.feedbackmanagement.dto.response.PagedResponse;
import com.feedbackmanagement.entity.FeedbackStatus;
import com.feedbackmanagement.security.CustomUserDetails;
import com.feedbackmanagement.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback management APIs")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Submit feedback", description = "Creates a new feedback entry")
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(
            @Valid @RequestBody FeedbackRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        FeedbackResponse response = feedbackService.createFeedback(request, currentUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all feedbacks", description = "Returns paginated list of all feedbacks with optional search and status filter")
    public ResponseEntity<ApiResponse<PagedResponse<FeedbackResponse>>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) FeedbackStatus status) {
        PagedResponse<FeedbackResponse> response;
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = feedbackService.searchFeedbacks(keyword, page, size, sortBy, sortDir);
        } else if (status != null) {
            response = feedbackService.getFeedbacksByStatus(status, page, size, sortBy, sortDir);
        } else {
            response = feedbackService.getAllFeedbacks(page, size, sortBy, sortDir);
        }
        return ResponseEntity.ok(ApiResponse.success("Feedbacks retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Returns a single feedback by its ID")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedbackById(@PathVariable Long id) {
        FeedbackResponse response = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feedback", description = "Updates an existing feedback entry")
    public ResponseEntity<ApiResponse<FeedbackResponse>> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        FeedbackResponse response = feedbackService.updateFeedback(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Feedback updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Deletes a feedback entry (owner or admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        feedbackService.deleteFeedback(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Feedback deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get feedbacks by user", description = "Returns paginated list of feedbacks by a specific user")
    public ResponseEntity<ApiResponse<PagedResponse<FeedbackResponse>>> getFeedbacksByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PagedResponse<FeedbackResponse> response = feedbackService.getFeedbacksByUser(userId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("User feedbacks retrieved successfully", response));
    }
}