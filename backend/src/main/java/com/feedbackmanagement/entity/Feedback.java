package com.feedbackmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks", indexes = {
        @Index(name = "idx_feedbacks_user_id", columnList = "user_id"),
        @Index(name = "idx_feedbacks_title", columnList = "title"),
        @Index(name = "idx_feedbacks_status", columnList = "status"),
        @Index(name = "idx_feedbacks_rating", columnList = "rating"),
        @Index(name = "idx_feedbacks_is_deleted", columnList = "is_deleted"),
        @Index(name = "idx_feedbacks_created_at", columnList = "created_at"),
        @Index(name = "idx_feedbacks_user_status", columnList = "user_id, status"),
        @Index(name = "idx_feedbacks_user_deleted", columnList = "user_id, is_deleted")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE feedbacks SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "rating", nullable = false, columnDefinition = "TINYINT")
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('NEW','REVIEWED','RESOLVED') DEFAULT 'NEW'")
    @Builder.Default
    private FeedbackStatus status = FeedbackStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_feedbacks_user_id"))
    private User user;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}