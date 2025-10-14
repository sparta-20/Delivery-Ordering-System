package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderRequestDto;
import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderStatusEnum;
import com.delivery.domain.order.repository.OrderRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public List<OrderResponseDto.OrderListDto> getOrderList(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    public List<OrderResponseDto.OrderListDto> getOrdersByOwner(Long ownerId) {
        // 수정 필요
        List<Order> orders = orderRepository.findByOwnerId(ownerId);
        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    public List<OrderResponseDto.AllOrderListDto> getAllList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseDto.AllOrderListDto::from)
                .toList();
    }
    
    @Transactional
    public void cancelOrder(Long userId, UUID orderId, OrderRequestDto.CancelOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        validateOrder(order, userId);
        order.cancel(dto.getReason());
    }

    private Order findOrderByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    private void validateOrder(Order order, Long userId) {
        if (!order.getUser().getUserId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);
        if (order.getStatus() != OrderStatusEnum.PENDING)
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        if (Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5) {
            throw new BusinessException(ErrorCode.ORDER_CANCEL_TIME_EXCEEDED);
    }
}
