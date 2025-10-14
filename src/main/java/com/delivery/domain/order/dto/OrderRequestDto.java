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
        private OrderStatusEnum status;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectOrderDto {
        private String reason;
    }
  
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelOrderDto {
        private String reason;
    }
}
