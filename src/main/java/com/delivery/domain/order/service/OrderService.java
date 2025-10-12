package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.user.entity.User;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto.OrderListDto> getOrderList(Long userId);
    // 수정 필요
    List<OrderResponseDto.OrderListDto> getOrdersByOwner(Long ownerId);
}
