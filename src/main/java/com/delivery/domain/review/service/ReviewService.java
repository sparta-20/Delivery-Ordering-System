package com.delivery.domain.review.service;

import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;
import com.delivery.domain.review.dto.ReviewSearchRes;
import com.delivery.domain.review.dto.ReviewUpdateReq;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface ReviewService {

    // 리뷰 생성
    ReviewRes createReview(Long userId, ReviewCreateReq request);

    // 리뷰 조회
    ReviewRes getReview(Long userId, UserRoleEnum role, UUID reviewId);

    // 리뷰 수정
    ReviewRes updateReview(Long userId, UUID reviewId, ReviewUpdateReq request);

    // 리뷰 삭제 (Soft Delete)
    void deleteReview(Long userId, UserRoleEnum role, UUID reviewId);

    Page<ReviewSearchRes> searchReviews(UUID storeId, int rating, Long writerId, int page, int size, Sort.Direction direction, User user);
}