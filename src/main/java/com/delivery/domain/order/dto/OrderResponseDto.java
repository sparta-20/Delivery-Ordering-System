package com.delivery.domain.order.dto;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderMenu;
import com.delivery.domain.order.entity.OrderMenuStatus;
import com.delivery.domain.order.entity.OrderStatus;
import lombok.*;

import java.util.List;
import java.util.UUID;

public class OrderResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderMenuDetailDto {
        private UUID menuId;
        private Integer quantity;
        private Integer price;
        private OrderMenuStatus status;

        public static OrderMenuDetailDto from(OrderMenu orderMenu) {
            return OrderMenuDetailDto.builder()
                    .menuId(orderMenu.getMenuId())
                    .quantity(orderMenu.getQuantity())
                    .price(orderMenu.getPrice())
                    .status(orderMenu.getStatus())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderListDto {
        private UUID orderId;
        private String address;
        private OrderStatus status;
        private Integer totalPrice;
        private List<OrderMenuDetailDto> menus;

        public static OrderListDto from(Order order) {
            return OrderListDto.builder()
                    .orderId(order.getOrderId())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .menus(order.getOrderMenus().stream()
                            .map(OrderMenuDetailDto::from)
                            .toList())
                    .build();
        }
    }
}
