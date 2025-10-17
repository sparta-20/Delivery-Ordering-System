package com.delivery.domain.store.category.dto;

import com.delivery.domain.store.category.entity.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoreCategoryRes {

    private UUID categoryId;
    private String categoryName;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public StoreCategoryRes(StoreCategory ct) {
        this.categoryId = ct.getCategoryId();
        this.categoryName = ct.getCategoryName();
        this.isActive = ct.isActive();
        this.createdAt = ct.getCreatedAt();
        this.modifiedAt = ct.getModifiedAt();
    }

    public static StoreCategoryRes from(StoreCategory ct) {
        return new StoreCategoryRes(ct);
    }
}
