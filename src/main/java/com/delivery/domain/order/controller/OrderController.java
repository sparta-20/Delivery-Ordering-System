package com.delivery.domain.order.controller;

import com.delivery.domain.order.dto.OrderReq;
import com.delivery.domain.order.dto.OrderRes;
import com.delivery.domain.order.service.OrderService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
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
    public ResponseEntity<ApiRes<List<OrderRes.OrderListDto>>> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<OrderRes.OrderListDto> list = orderService.getOrderList(user.getUserId());
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner")
    public ResponseEntity<ApiRes<List<OrderRes.OrderListDto>>> getOwnerOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<OrderRes.OrderListDto> list = orderService.getOrdersByOwner(user.getUserId());
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/owner/{orderId}")
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> getOwnerOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable UUID orderId) {
        User user = userDetails.getUser();
        OrderRes.OrderDetailDto result = orderService.getOrderDetail(user.getUserId(), orderId);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PatchMapping("/owner/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable UUID orderId,
                                                  @RequestBody OrderReq.ChangeOrderStatusDto dto) {
        // TODO: ApiRes 사용해서 수정
        User user = userDetails.getUser();
        orderService.changeStatus(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PatchMapping("/owner/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderReq.RejectOrderDto dto) {
        User user = userDetails.getUser();
        orderService.rejectOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }
      
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin")
    public ResponseEntity<ApiRes<List<OrderRes.AllOrderListDto>>> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderRes.AllOrderListDto> list = orderService.getAllList();
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderReq.CancelOrderDto dto) {
        User user = userDetails.getUser();
        orderService.cancelOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/detail")
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> getOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @PathVariable UUID orderId) {
        User user = userDetails.getUser();
        OrderRes.OrderDetailDto result = orderService.getOrderDetail(user.getUserId(), orderId);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderRes.OrderDetailDto>> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderRes.OrderDetailDto order = orderService.createOrder(userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRes.OrderDetailDto>> getOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderRes.OrderDetailDto order = orderService.getOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
