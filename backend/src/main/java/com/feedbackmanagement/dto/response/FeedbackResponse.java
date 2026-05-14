package com.feedbackmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long id;
    private String title;
    private String message;
    private Integer rating;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userName;
}