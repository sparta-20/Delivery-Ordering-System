package com.delivery.domain.review.repository;

import com.delivery.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    /**
     * 리뷰 검색
     * - storeId, rating, writerId로 동적 검색
     * - 삭제된 리뷰 제외
     * - N+1 방지를 위한 JOIN FETCH
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.store s " +
            "JOIN FETCH r.user u " +
            "WHERE (:storeId IS NULL OR s.storeId = :storeId) " +
            "AND (:rating IS NULL OR r.rating = :rating) " +
            "AND (:writerId IS NULL OR u.userId = :writerId) " +
            "AND r.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND u.deletedAt IS NULL")
    Page<Review> searchReviews(
            @Param("storeId") UUID storeId,
            @Param("rating") Integer rating,
            @Param("writerId") Long writerId,
            Pageable pageable
    );

    /**
     * 가게별 평점 평균 계산
     * - 가게 목록 조회 시 사용
     * - N+1 방지를 위해 별도 메서드로 분리
     */
    @Query("SELECT AVG(r.rating) FROM Review r " +
            "WHERE r.store.storeId = :storeId " +
            "AND r.deletedAt IS NULL")
    Double getAverageRatingByStoreId(@Param("storeId") UUID storeId);
}