package com.delivery.store.dto;

import com.delivery.store.entity.Store;
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

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.city = store.getCity();
        this.district = store.getDistrict();
        this.minPrice = store.getMinPrice();
        this.status = store.getStatus().name();


        if (store.getCategory() != null) {
            this.categoryId = store.getCategory().getId();
            this.categoryName = store.getCategory().getName();
        }

        this.ownerUserId = store.getOwnerUserId();
        this.createdAt = store.getCreatedAt();
        this.modifiedAt = store.getModifiedAt();

    }

}
