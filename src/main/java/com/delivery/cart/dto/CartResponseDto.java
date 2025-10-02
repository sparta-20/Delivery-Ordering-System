package com.delivery.cart.dto;

import com.delivery.cart.entity.CartItem;
import lombok.*;

import java.util.List;
import java.util.UUID;

public class CartResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class CartListDto {
        private UUID cartId;
        private UUID storeId;
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

        public static CartItemDetailDto from(CartItem item) {
            return CartItemDetailDto.builder()
                    .cartItemId(item.getCartMenuId())
                    .menuId(item.getMenuId())
                    .menuName("메뉴 이름 " + item.getMenuId()) // 추후 수정 필요
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .totalPrice(item.getPrice() * item.getQuantity())
                    .build();
        }
    }
}
