package com.delivery.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class StoreResponseDto {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private String district;
    private Integer minPrice;
    private String status;

    private Integer categoryId;
    private String categoryName;

    private Long ownerUserId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
