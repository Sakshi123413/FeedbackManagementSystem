package com.feedbackmanagement.repository;

import com.feedbackmanagement.entity.Feedback;
import com.feedbackmanagement.entity.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByUserId(Long userId, Pageable pageable);

    Page<Feedback> findByStatus(FeedbackStatus status, Pageable pageable);

    Page<Feedback> findByUserIdAndStatus(Long userId, FeedbackStatus status, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE " +
           "LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Feedback> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.user.id = :userId AND " +
           "(LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.message) LIKE LOWER(CONCAT('%', :keyword, '%')))" )
    Page<Feedback> searchByKeywordAndUserId(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);

    long countByUserId(Long userId);
}