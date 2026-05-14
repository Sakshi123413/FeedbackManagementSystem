package com.feedbackmanagement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackUpdateRequest {

    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Size(min = 10, max = 1000, message = "Message must be between 10 and 1000 characters")
    private String message;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    private String status;
}