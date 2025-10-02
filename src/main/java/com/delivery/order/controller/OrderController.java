package com.delivery.order.controller;

import com.delivery.order.dto.OrderResponseDto;
import com.delivery.order.service.OrderService;
import com.delivery.order.service.OrderServiceImpl;
import com.delivery.security.UserDetailsImpl;
import com.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<OrderResponseDto.OrderListDto> list = orderService.getOrderList(user.getUserId());
        return ResponseEntity.ok(list);
    }
}
