package com.delivery.order.controller;

import com.delivery.order.dto.OrderResponseDto;
import com.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    // 추후 JWT 기반 유저 정보 수정
    public ResponseEntity<?> getOrders(@RequestParam Long userId) {
        List<OrderResponseDto.OrderListDto> list = orderService.getOrderList(userId);
        return ResponseEntity.ok(list);
    }
}
