package com.delivery.order.service;

import com.delivery.order.dto.OrderResponseDto;
import com.delivery.order.entity.Order;
import com.delivery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponseDto.OrderListDto> getOrderList(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }
}
