package com.delivery.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CartRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCartItemDto {
        private UUID menuId;
        private Integer quantity;
    }
}
