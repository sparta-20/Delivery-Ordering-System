package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderRequestDto;
import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderResponseDto.OrderListDto> getOrderList(Long userId);
    // 수정 필요
    List<OrderResponseDto.OrderListDto> getOrdersByOwner(Long ownerId);
    void changeStatus(Long userId, UUID orderId, OrderRequestDto.ChangeOrderStatusDto dto);
}
