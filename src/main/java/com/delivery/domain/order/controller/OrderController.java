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

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/owner/{orderId}")
    public ResponseEntity<OrderResponseDto.OrderDetailDto> getOwnerOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable UUID orderId) {
        User user = userDetails.getUser();
        OrderResponseDto.OrderDetailDto result = orderService.getOrderDetail(user.getUserId(), orderId);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PatchMapping("/owner/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable UUID orderId,
                                                  @RequestBody OrderRequestDto.ChangeOrderStatusDto dto) {
        // TODO: ApiResponse 사용해서 수정
        // TODO: store 이용해서 수정
        User user = userDetails.getUser();
        orderService.changeStatus(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PatchMapping("/owner/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderRequestDto.RejectOrderDto dto) {
        // TODO
        User user = userDetails.getUser();
        orderService.rejectOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }
      
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(orderService.getAllList());
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
