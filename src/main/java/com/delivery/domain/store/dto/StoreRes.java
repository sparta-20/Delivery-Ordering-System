package com.delivery.domain.store.dto;

import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class StoreRes {

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

    public StoreRes(Store store) {
        this.storeId = store.getStoreId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.city = store.getCity();
        this.district = store.getDistrict();
        this.minPrice = store.getMinPrice();
        this.status = store.getStatus()!= null ? store.getStatus().name() : StoreStatusEnum.ACTIVE.name();

        if (store.getCategory() != null) {
            this.categoryId = store.getCategory().getCategoryId();
            this.categoryName = store.getCategory().getCategoryName();
        }

        if (store.getOwner() != null) {
            this.ownerUserId = store.getOwner().getUserId();
        }

        this.createdAt = store.getCreatedAt();
        this.modifiedAt = store.getModifiedAt();
    }

    public static StoreRes from(Store store) {
        return new StoreRes(store);
    }


}