package com.delivery.domain.review.dto;

import com.delivery.domain.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "가게별 리뷰 응답")
@Getter
@Builder
public class PublicReviewRes {
    private UUID reviewId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;

    // 작성자 정보
    private UserInfo  user;
    // 주문 정보
    private OrderInfo order;

    @Getter
    @Builder
    public static class UserInfo  {
        private Long userId;
        private String nickname;
    }

    @Getter
    @Builder
    public static class OrderInfo {
        private UUID orderId;
        private LocalDateTime orderDate;
    }

    public static PublicReviewRes from(Review review) {
        return PublicReviewRes.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .user(PublicReviewRes.UserInfo.builder()
                        .userId(review.getUser().getUserId())
                        .nickname(review.getUser().getNickname())
                        .build())
                .order(PublicReviewRes.OrderInfo.builder()
                        .orderId(review.getOrder().getOrderId())
                        .orderDate(review.getOrder().getCreatedAt())
                        .build())
                .build();
    }
}
