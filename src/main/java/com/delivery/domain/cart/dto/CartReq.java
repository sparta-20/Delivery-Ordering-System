package com.delivery.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CartReq {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCartItemDto {
        private UUID menuId;
        private Integer quantity;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCartItemDto {
        private Integer quantity;
    }
}
