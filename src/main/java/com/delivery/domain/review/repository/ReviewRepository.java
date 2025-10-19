package com.delivery.domain.review.repository;

import com.delivery.domain.review.dto.StoreReviewStats;
import com.delivery.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /** 고객 */
    // 주문당 1 리뷰 제약 확인
    boolean existsByOrder_OrderIdAndDeletedAtIsNull(UUID orderId);

    // 사용자 소유 리뷰 조회 (수정/삭제용)
    // 작성자 확인 포함
    // Lazy Loading
    Optional<Review> findByReviewIdAndUser_UserIdAndDeletedAtIsNull(UUID reviewId, Long userId);

    // 리뷰 단건 상세 조회 (Fetch Join)
    // - User/Order/Store 즉시 로딩
    // - 가게 삭제에도 리뷰 이력은 조회 가능
    @Query("""
        SELECT r FROM Review r
          JOIN FETCH r.user u
          JOIN FETCH r.store s
          JOIN FETCH r.order o
         WHERE r.reviewId = :reviewId
           AND r.deletedAt IS NULL
           AND u.deletedAt IS NULL
           AND o.deletedAt IS NULL
    """)
    Optional<Review> findByReviewIdWithUser(@Param("reviewId") UUID reviewId);

    // 사용자별 단건 상세 조회 (Fetch Join)
    // - 작성자 확인 포함
    // - User/Order/Store 즉시 로딩
    // - 가게 삭제에도 리뷰 이력은 조회 가능
    @Query("""
            SELECT r FROM Review r
              JOIN FETCH r.user u
              JOIN FETCH r.order o
              JOIN FETCH r.store s
             WHERE r.reviewId = :reviewId
               AND u.userId   = :userId
               AND r.deletedAt IS NULL
               AND u.deletedAt IS NULL
               AND o.deletedAt IS NULL
            """)
    Optional<Review> findUserReviewDetail(@Param("userId") Long userId, @Param("reviewId") UUID reviewId);

    // 내 리뷰 목록 조회 (Fetch Join)
    // - User/Order/Store 즉시 로딩
    // - 가게 삭제는 상관없음 (리뷰 이력 유지)
    @Query("""
            SELECT r FROM Review r
              JOIN FETCH r.user u
              JOIN FETCH r.store s
              JOIN FETCH r.order o
             WHERE u.userId = :userId
               AND r.deletedAt IS NULL
               AND u.deletedAt IS NULL
               AND o.deletedAt IS NULL
            """)
    Page<Review> searchMyReviews(@Param("userId") Long userId, Pageable pageable);

    /**
     * 관리자
     */
    // 리뷰 ID로 삭제되지 않은 리뷰 조회 (관리자 삭제용)
    Optional<Review> findByReviewIdAndDeletedAtIsNull(UUID reviewId);

    // 관리자용 검색 (Fetch Join)
    // - User/Order/Store 즉시 로딩
    // - 삭제된 리뷰도 조회 가능
    // - 가게 삭제는 상관없음 (리뷰 이력 유지)
    @Query("""
        SELECT r FROM Review r
          JOIN r.user u
          JOIN r.store s
          JOIN r.order o
         WHERE (:includeDeleted = TRUE OR r.deletedAt IS NULL)
           AND (:userId IS NULL OR u.userId = :userId)
           AND (:storeId IS NULL OR s.storeId = :storeId)
           AND (:rating IS NULL OR r.rating = :rating)
    """)
    Page<Review> searchReviewsForAdmin(@Param("userId") Long userId,
                                       @Param("storeId") UUID storeId,
                                       @Param("rating") Integer rating,
                                       @Param("includeDeleted") boolean includeDeleted,
                                       Pageable pageable
    );

    // 관리자용 리뷰 단건 상세 조회 (Fetch Join)
    // - User/Order/Store 즉시 로딩
    // - 삭제된 리뷰도 조회 가능
    @Query("""
        SELECT r FROM Review r
          JOIN FETCH r.user u
          JOIN FETCH r.store s
          JOIN FETCH r.order o
         WHERE r.reviewId = :reviewId
           AND (:includeDeleted = TRUE OR r.deletedAt IS NULL)
    """)
    Optional<Review> findReviewDetailForAdmin(@Param("reviewId") UUID reviewId,
                                              @Param("includeDeleted") boolean includeDeleted
    );

    /**
     * 사장
     */
    // 내 가게 리뷰 목록 조회 (사장 전용: 삭제 제외, Fetch join)
    @Query("""
       SELECT r FROM Review r
         JOIN r.store s
         JOIN r.user u
         JOIN r.order o
        WHERE r.deletedAt IS NULL
          AND s.storeId = :storeId
          AND (:rating IS NULL OR r.rating = :rating)
    """)
    Page<Review> findStoreReviews(@Param("storeId") UUID storeId,
                                  @Param("rating") Integer rating,
                                  Pageable pageable);

    // 리뷰 단건 조회 (사장 전용: 삭제 제외, Fetch join)
    @Query("""
       SELECT r FROM Review r
         JOIN FETCH r.store s
         JOIN FETCH r.user u
         JOIN FETCH r.order o
        WHERE r.reviewId = :reviewId
          AND r.deletedAt IS NULL
          AND s.storeId = :storeId
    """)
    Optional<Review> findStoreReviewDetail(@Param("storeId") UUID storeId,
                                           @Param("reviewId") UUID reviewId);

    /** 공개 */
    // 공개용 가게 리뷰 목록
    // - 삭제되지 않은 리뷰만
    // - 가게가삭제되지 않은 경우만
    @Query("""
       SELECT r FROM Review r
         JOIN FETCH r.store s
         JOIN FETCH r.user u
         JOIN FETCH r.order o
        WHERE r.deletedAt IS NULL
          AND s.deletedAt IS NULL
          AND s.storeId = :storeId
    """)
    Page<Review> findPublicStoreReviews(@Param("storeId") UUID storeId, Pageable pageable);

    @Query("""
        SELECT new com.delivery.domain.review.dto.StoreReviewStats(
            s.storeId,
            s.name,
            COUNT(r),
            COALESCE(AVG(r.rating), 0)
        )
        FROM Review r
        JOIN r.store s
        WHERE r.deletedAt IS NULL
          AND s.deletedAt IS NULL
          AND s.storeId = :storeId
        GROUP BY s.storeId, s.name
    """)
    Optional<StoreReviewStats> findStoreReviewStats(@Param("storeId") UUID storeId);
}