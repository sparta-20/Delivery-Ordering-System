package com.delivery.domain.cart.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

public class CartRes {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartListDto {
        private UUID cartId;
        private UUID storeId;
        private String storeName;
        private Integer totalPrice;
        List<CartItemDetailDto> items;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartItemDetailDto {
        private UUID cartItemId;
        private UUID menuId;
        private String menuName;
        private Integer quantity;
        private Integer price;
        private Integer totalPrice;
    }
}
