package com.delivery.domain.review.dto;

import com.delivery.domain.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "리뷰 기본 응답")
@Getter
@Builder
public class ReviewRes {

    private UUID reviewId;
    private UUID orderId;
    private UUID storeId;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static ReviewRes from(Review review) {
        return ReviewRes.builder()
                .reviewId(review.getReviewId())
                .storeId(review.getStore().getStoreId())
                .orderId(review.getOrder().getOrderId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getModifiedAt())
                .build();
    }
}