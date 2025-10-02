package com.delivery.order.service;

import com.delivery.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto.OrderListDto> getOrderList(Long userId);
}
