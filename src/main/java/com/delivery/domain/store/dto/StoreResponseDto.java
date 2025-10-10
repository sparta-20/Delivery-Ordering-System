package com.delivery.domain.store.dto;

import com.delivery.domain.store.entity.Store;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class StoreResponseDto {

    private UUID storeId;
    private String name;
    private String address;
    private String city;
    private String district;
    private Integer minPrice;
    private String status;

    private UUID categoryId;
    private String categoryName;

    private Long ownerUserId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.city = store.getCity();
        this.district = store.getDistrict();
        this.minPrice = store.getMinPrice();
        this.status = store.getStatus().name();

        if (store.getCategoryId() != null) {
            this.categoryId = store.getCategoryId().getCategoryId();
            this.categoryName = store.getCategoryId().getCategoryName();
        }

        this.ownerUserId = store.getOwnerUserId();
        this.createdAt = store.getCreatedAt();
        this.modifiedAt = store.getModifiedAt();
    }

    public static StoreResponseDto from(Store store) {
        return new StoreResponseDto(store);
    }


}
