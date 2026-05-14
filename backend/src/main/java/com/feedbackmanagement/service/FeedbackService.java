package com.feedbackmanagement.service;

import com.feedbackmanagement.dto.request.FeedbackRequest;
import com.feedbackmanagement.dto.request.FeedbackUpdateRequest;
import com.feedbackmanagement.dto.response.FeedbackResponse;
import com.feedbackmanagement.dto.response.PagedResponse;
import com.feedbackmanagement.entity.FeedbackStatus;
import com.feedbackmanagement.security.CustomUserDetails;

public interface FeedbackService {

    FeedbackResponse createFeedback(FeedbackRequest request, CustomUserDetails currentUser);

    PagedResponse<FeedbackResponse> getAllFeedbacks(int page, int size, String sortBy, String sortDir);

    PagedResponse<FeedbackResponse> getFeedbacksByStatus(FeedbackStatus status, int page, int size, String sortBy, String sortDir);

    FeedbackResponse getFeedbackById(Long id);

    FeedbackResponse updateFeedback(Long id, FeedbackUpdateRequest request, CustomUserDetails currentUser);

    void deleteFeedback(Long id, CustomUserDetails currentUser);

    PagedResponse<FeedbackResponse> searchFeedbacks(String keyword, int page, int size, String sortBy, String sortDir);

    PagedResponse<FeedbackResponse> getFeedbacksByUser(Long userId, int page, int size, String sortBy, String sortDir);
}