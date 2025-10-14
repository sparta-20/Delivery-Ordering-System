package com.delivery.domain.order.controller;

import com.delivery.domain.order.dto.OrderRequestDto;
import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.service.OrderService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner")
    public ResponseEntity<?> getOwnerOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        // 추후 store -> 수정 (현재는 사장이 로그인했다고 가정하고 ID로 찾음)
        return ResponseEntity.ok(orderService.getOrdersByOwner(user.getUserId()));
    }
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderRequestDto.CancelOrderDto dto) {
        User user = userDetails.getUser();
        orderService.cancelOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }
}
