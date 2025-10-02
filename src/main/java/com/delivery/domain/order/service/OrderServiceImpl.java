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
}
