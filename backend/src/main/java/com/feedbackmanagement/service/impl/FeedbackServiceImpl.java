package com.feedbackmanagement.service.impl;

import com.feedbackmanagement.dto.request.FeedbackRequest;
import com.feedbackmanagement.dto.request.FeedbackUpdateRequest;
import com.feedbackmanagement.dto.response.FeedbackResponse;
import com.feedbackmanagement.dto.response.PagedResponse;
import com.feedbackmanagement.entity.Feedback;
import com.feedbackmanagement.entity.FeedbackStatus;
import com.feedbackmanagement.entity.User;
import com.feedbackmanagement.exception.BadRequestException;
import com.feedbackmanagement.exception.ResourceNotFoundException;
import com.feedbackmanagement.exception.UnauthorizedException;
import com.feedbackmanagement.repository.FeedbackRepository;
import com.feedbackmanagement.repository.UserRepository;
import com.feedbackmanagement.security.CustomUserDetails;
import com.feedbackmanagement.service.FeedbackService;
import com.feedbackmanagement.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final PaginationUtils paginationUtils;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request, CustomUserDetails currentUser) {
        log.info("Creating feedback for user: {}", currentUser.getEmail());

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", currentUser.getId()));

        Feedback feedback = Feedback.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .rating(request.getRating())
                .user(user)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback created successfully with id: {}", savedFeedback.getId());
        return mapToResponse(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<FeedbackResponse> getAllFeedbacks(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all feedbacks - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        Pageable pageable = paginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<Feedback> feedbackPage = feedbackRepository.findAll(pageable);
        return paginationUtils.mapToPagedResponse(feedbackPage.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<FeedbackResponse> getFeedbacksByStatus(FeedbackStatus status, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching feedbacks with status: {}", status);
        Pageable pageable = paginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<Feedback> feedbackPage = feedbackRepository.findByStatus(status, pageable);
        return paginationUtils.mapToPagedResponse(feedbackPage.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long id) {
        log.info("Fetching feedback with id: {}", id);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));
        return mapToResponse(feedback);
    }

    @Override
    @Transactional
    public FeedbackResponse updateFeedback(Long id, FeedbackUpdateRequest request, CustomUserDetails currentUser) {
        log.info("Updating feedback id: {} by user: {}", id, currentUser.getEmail());

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));

        validateFeedbackOwnership(feedback, currentUser, "update");

        applyUpdates(feedback, request);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback updated successfully with id: {}", updatedFeedback.getId());
        return mapToResponse(updatedFeedback);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id, CustomUserDetails currentUser) {
        log.info("Deleting feedback id: {} by user: {}", id, currentUser.getEmail());

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", id));

        if (!feedback.getUser().getId().equals(currentUser.getId()) &&
                currentUser.getRole() != com.feedbackmanagement.entity.Role.ADMIN) {
            log.warn("Unauthorized delete attempt on feedback id: {} by user: {}", id, currentUser.getEmail());
            throw new UnauthorizedException("You are not authorized to delete this feedback");
        }

        feedbackRepository.delete(feedback);
        log.info("Feedback deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<FeedbackResponse> searchFeedbacks(String keyword, int page, int size, String sortBy, String sortDir) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword must not be empty");
        }
        log.info("Searching feedbacks with keyword: {}", keyword);
        Pageable pageable = paginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<Feedback> feedbackPage = feedbackRepository.searchByKeyword(keyword.trim(), pageable);
        return paginationUtils.mapToPagedResponse(feedbackPage.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<FeedbackResponse> getFeedbacksByUser(Long userId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching feedbacks for user id: {}", userId);
        Pageable pageable = paginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<Feedback> feedbackPage = feedbackRepository.findByUserId(userId, pageable);
        return paginationUtils.mapToPagedResponse(feedbackPage.map(this::mapToResponse));
    }

    private void validateFeedbackOwnership(Feedback feedback, CustomUserDetails currentUser, String action) {
        if (!feedback.getUser().getId().equals(currentUser.getId())) {
            log.warn("Unauthorized {} attempt on feedback id: {} by user: {}", action, feedback.getId(), currentUser.getEmail());
            throw new UnauthorizedException("You are not authorized to " + action + " this feedback");
        }
    }

    private void applyUpdates(Feedback feedback, FeedbackUpdateRequest request) {
        if (request.getTitle() != null) {
            feedback.setTitle(request.getTitle());
        }
        if (request.getMessage() != null) {
            feedback.setMessage(request.getMessage());
        }
        if (request.getRating() != null) {
            feedback.setRating(request.getRating());
        }
        if (request.getStatus() != null) {
            try {
                feedback.setStatus(FeedbackStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status value. Allowed: NEW, REVIEWED, RESOLVED");
            }
        }
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .title(feedback.getTitle())
                .message(feedback.getMessage())
                .rating(feedback.getRating())
                .status(feedback.getStatus().name())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .userId(feedback.getUser().getId())
                .userName(feedback.getUser().getName())
                .build();
    }
}