package com.delivery.domain.review.service;

import com.delivery.domain.review.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface ReviewService {

    /** Customer */
    ReviewRes createReview(Long userId, ReviewCreateReq request);
    CustomerReviewDetailRes getMyReview(Long userId, UUID reviewId);
    Page<CustomerReviewDetailRes> getMyReviews(Long userId, int page, int size, Sort.Direction direction);
    ReviewRes updateReview(Long userId, UUID reviewId, ReviewUpdateReq request);
    void deleteReview(Long userId, UUID reviewId);

    /** Admin */
    Page<AdminReviewDetailRes> searchReviewsForAdmin(
            Long userId, UUID storeId, Integer rating, boolean includeDeleted,
            int page, int size, Sort.Direction direction
    );
    AdminReviewDetailRes getReviewDetailForAdmin(UUID reviewId, boolean includeDeleted);
    void deleteReviewByAdmin(Long userId, UUID reviewId);
    AdminReviewDetailRes restoreReview(UUID reviewId);

    /** Owner */
    Page<OwnerReviewDetailRes> getMyStoreReviews(
            Long ownerId, UUID storeId,
            Integer rating, int page, int size, Sort.Direction direction
    );
    OwnerReviewDetailRes getMyStoreReviewDetail(Long ownerId, UUID storeId, UUID reviewId);

    /** 공개 */
    Page<PublicReviewRes> getStoreReviews(UUID storeId, int page, int size, Sort.Direction direction);

    StoreReviewStats getStoreReviewStats(UUID storeId);
}