package com.delivery.domain.review.dto;

import com.delivery.domain.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "고객용 리뷰 상세 응답")
@Getter
@Builder
public class CustomerReviewDetailRes {
    private UUID reviewId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 가게 정보
    private StoreInfo store;
    // 주문 정보
    private OrderInfo order;

    @Getter
    @Builder
    public static class StoreInfo {
        private UUID storeId;
        private String storeName;
    }

    @Getter
    @Builder
    public static class OrderInfo {
        private UUID orderId;
        private LocalDateTime orderDate;
    }

    public static CustomerReviewDetailRes from(Review review) {
        return CustomerReviewDetailRes.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getModifiedAt())
                .store(CustomerReviewDetailRes.StoreInfo.builder()
                        .storeId(review.getStore().getStoreId())
                        .storeName(review.getStore().getName())
                        .build())
                .order(CustomerReviewDetailRes.OrderInfo.builder()
                        .orderId(review.getOrder().getOrderId())
                        .orderDate(review.getOrder().getCreatedAt())
                        .build())
                .build();
    }
}
