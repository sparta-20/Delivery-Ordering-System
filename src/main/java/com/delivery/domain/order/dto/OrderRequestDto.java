package com.delivery.domain.order.dto;

import com.delivery.domain.order.entity.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeOrderStatusDto {
        private Long ownerId; // TODO: 추후 제거
        private OrderStatusEnum status;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectOrderDto {
        private Long ownerId; // TODO: 추후 제거
        private String reason;
    }
}
