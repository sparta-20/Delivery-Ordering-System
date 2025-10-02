package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto.OrderListDto> getOrderList(Long userId);
}
