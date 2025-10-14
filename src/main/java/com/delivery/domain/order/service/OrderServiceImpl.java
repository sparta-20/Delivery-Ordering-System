package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
