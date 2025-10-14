package com.delivery.domain.review.service;

import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;

public interface ReviewService {

    // 리뷰 생성
    ReviewRes createReview(Long userId, ReviewCreateReq request);
}