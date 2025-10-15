package com.delivery.domain.review.repository;

import com.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // 주문 ID로 삭제되지 않은 리뷰 존재 여부 확인
    boolean existsByOrder_OrderIdAndDeletedAtIsNull(UUID orderId);

    // 리뷰 ID로 삭제되지 않은 리뷰 조회
    Optional<Review> findByReviewIdAndDeletedAtIsNull(UUID reviewId);

    /**
     * 리뷰 조회 (User 정보 함께 조회)
     * - 삭제되지 않은 리뷰만 조회
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user " +
            "WHERE r.reviewId = :reviewId " +
            "AND r.deletedAt IS NULL")
    Optional<Review> findByReviewIdWithUser(@Param("reviewId") UUID reviewId);

    /**
     * 리뷰 조회 (User, Order 정보 함께 조회)
     * - 삭제되지 않은 리뷰만 조회
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.order " +
            "WHERE r.reviewId = :reviewId " +
            "AND r.deletedAt IS NULL")
    Optional<Review> findByReviewIdWithUserAndOrder(@Param("reviewId") UUID reviewId);

}