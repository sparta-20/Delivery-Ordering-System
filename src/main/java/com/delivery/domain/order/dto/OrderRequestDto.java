package com.delivery.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelOrderDto {
        private String reason;
    }
}
