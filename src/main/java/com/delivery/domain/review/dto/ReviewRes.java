package com.delivery.domain.review.dto;

import com.delivery.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReviewRes {

    private UUID reviewId;
    private Long storeId;
    private UUID orderId;
    private Long userId;
    private String nickname;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static ReviewRes from(Review review, UUID orderId, Long userId, String nickname) {
        return ReviewRes.builder()
                .reviewId(review.getReviewId())
                .storeId(review.getStoreId())
                .orderId(orderId)
                .userId(userId)
                .nickname(nickname)
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getModifiedAt())
                .build();
    }
}