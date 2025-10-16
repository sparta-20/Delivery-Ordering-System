package com.delivery.domain.order.dto;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderMenu;
import com.delivery.domain.order.entity.OrderMenuStatusEnum;
import com.delivery.domain.order.entity.OrderStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderRes {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderMenuDetailDto {
        private UUID menuId;
        private String menuName;
        private Integer quantity;
        private Integer price;
        private OrderMenuStatusEnum status;

        public static OrderMenuDetailDto from(OrderMenu orderMenu) {
            return OrderMenuDetailDto.builder()
                    .menuId(orderMenu.getMenu().getMenuId())
                    .menuName(orderMenu.getMenu().getName())
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
        private String storeName;
        private OrderStatusEnum status;
        private Integer totalPrice;
        private List<OrderMenuDetailDto> menus;

        public static OrderListDto from(Order order) {
            return OrderListDto.builder()
                    .orderId(order.getOrderId())
                    .address(order.getAddress())
                    .storeName(order.getStore().getName())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .menus(order.getOrderMenus().stream()
                            .map(OrderMenuDetailDto::from)
                            .toList())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class AllOrderListDto {
        private UUID orderId;
        private String storeName;
        private String address;
        private OrderStatusEnum status;
        private Integer totalPrice;
        private List<OrderMenuDetailDto> menus;

        public static AllOrderListDto from(Order order) {
            return AllOrderListDto.builder()
                    .orderId(order.getOrderId())
                    .storeName(order.getStore().getName())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .menus(order.getOrderMenus().stream()
                            .map(OrderMenuDetailDto::from)
                            .toList())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class OrderDetailDto {
        private UUID storeId;
        private String storeName;
        private List<OrderMenuDetailDto> menus;
        private LocalDateTime createdAt;
        private String payment;
        private Integer totalPrice;
        private Integer deliveryFee;
        private String phoneNumber;
        private String address;
        private String message;
        private String deliveryMessage;
        private String reason;

        public static OrderDetailDto from(Order order) {
            return OrderDetailDto.builder()
                    .storeId(order.getStore().getStoreId())
                    .storeName(order.getStore().getName())
                    .menus(order.getOrderMenus().stream()
                            .map(OrderMenuDetailDto::from)
                            .toList())
                    .createdAt(order.getCreatedAt())
                    .payment("CARD")
                    .totalPrice(order.getTotalPrice())
                    .deliveryFee(order.getDeliveryFee())
                    .phoneNumber(order.getPhoneNumber())
                    .address(order.getAddress())
                    .message(order.getMessage())
                    .deliveryMessage(order.getDeliveryMessage())
                    .reason(order.getCanceledReason())
                    .build();
        }
    }
}
