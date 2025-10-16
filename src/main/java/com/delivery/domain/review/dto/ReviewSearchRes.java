package com.delivery.domain.review.dto;

import com.delivery.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReviewSearchRes {
    private UUID reviewId;
    private UUID storeId;
    private String storeName;
    private Long userId;
    private String nickname;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewSearchRes from(Review review) {
        return ReviewSearchRes.builder()
                .reviewId(review.getReviewId())
                .storeId(review.getStore().getStoreId())
                .storeName(review.getStore().getName())
                .userId(review.getUser().getUserId())
                .nickname(review.getUser().getNickname())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getModifiedAt())
                .build();
    }
}
