package com.delivery.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

    @NotNull(message = "주소를 선택해주세요.")
    private UUID addressId;

    private String message;

    private String deliveryMessage;
}
