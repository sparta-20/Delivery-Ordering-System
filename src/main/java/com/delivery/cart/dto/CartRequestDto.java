package com.delivery.cart.dto;

import com.delivery.cart.entity.Cart;
import com.delivery.user.entity.User;
import lombok.Getter;

import java.util.UUID;

public class CartRequestDto {

    @Getter
    public static class CartInfoDto {
        private UUID menuId;
        private Integer quantity;

        // 추후 수정 필요 (Menu)
        public CartInfoDto(UUID menuId, Integer quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Cart toEntity(User user) {
            return Cart.builder()
                    .user(user)
                    .quantity(this.quantity)
                    .menuId(this.menuId)
                    .build();
        }
    }
}
