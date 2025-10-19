package com.delivery.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class StoreReviewStats {
    private UUID storeId;
    private String name;
    private long totalCount;
    private double avgRating;
}