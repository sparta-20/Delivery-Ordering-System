package com.delivery.domain.order.controller;

import com.delivery.domain.order.dto.CreateOrderReq;
import com.delivery.domain.order.dto.OrderReq;
import com.delivery.domain.order.dto.OrderRes;
import com.delivery.domain.order.service.OrderService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order", description = "주문 API")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "주문 내역 조회",
            description = "현재 로그인한 사용자의 주문 내역을 조회합니다. 기본정렬은 시간순, 지정한 크기에 따라 개수를 설정해 조회할 수 있습니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<ApiRes<Page<OrderRes.OrderListDto>>> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        User user = userDetails.getUser();
        Page<OrderRes.OrderListDto> list = orderService.getOrderList(
                user.getUserId(), page, size, direction);
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @Operation(
            summary = "주문 내역 조회 - 사장",
            description = "현재 로그인한 사용자 가게의 모든 주문 내역을 조회합니다. 기본정렬은 시간순, 지정한 크기에 따라 개수를 설정해 조회할 수 있습니다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner")
    public ResponseEntity<ApiRes<Page<OrderRes.OrderListDto>>> getOwnerOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size,
                                                                              @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        User user = userDetails.getUser();
        Page<OrderRes.OrderListDto> list = orderService.getOrdersByOwner(
                user.getUserId(), page, size, direction);
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @Operation(
            summary = "주문 내역 상세 조회 - 사장",
            description = "현재 로그인한 사용자 가게의 주문 내역을 상세 조회합니다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner/{orderId}")
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> getOwnerOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable UUID orderId) {
        User user = userDetails.getUser();
        OrderRes.OrderDetailDto result = orderService.getOrderDetail(user.getUserId(), orderId);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @Operation(
            summary = "주문 내역 조회 - 관리자",
            description = "관리자는 id로 주문 내역 상세 조회가 가능합니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> getAdminOrderDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable UUID orderId) {
        User user = userDetails.getUser();
        OrderRes.OrderDetailDto result = orderService.getAdminOrderDetail(user.getUserId(), orderId);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @Operation(
            summary = "주문 상태 변경 - 사장",
            description = "현재 로그인한 사용자 가게의 주문 내역을 선택해 주문 상태를 변경합니다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/owner/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable UUID orderId,
                                                  @RequestBody OrderReq.ChangeOrderStatusDto dto) {
        User user = userDetails.getUser();
        orderService.changeStatus(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주문 상태 변경 - 관리자",
            description = "관리자는 id로 주문 상태를 변경할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PatchMapping("/admin/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatusByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable UUID orderId,
                                                  @RequestBody OrderReq.ChangeOrderStatusDto dto) {
        User user = userDetails.getUser();
        orderService.changeStatusByAdmin(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주문 거절 - 사장",
            description = "현재 로그인한 사용자 가게의 주문 내역을 선택해 주문을 거절합니다. 거절 사유를 함께 작성해야 합니다."
    )
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/owner/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderReq.RejectOrderDto dto) {
        User user = userDetails.getUser();
        orderService.rejectOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주문 거절 - 관리자",
            description = "관리자는 id로 주문을 거절할 수 있습니다. 거절 사유를 함께 작성해야 합니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PatchMapping("/admin/{orderId}/reject")
    public ResponseEntity<Void> rejectOrderByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderReq.RejectOrderDto dto) {
        User user = userDetails.getUser();
        orderService.rejectOrderByAdmin(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주문 내역 조회 - 관리자",
            description = "관리자는 모든 주문 내역을 조회 가능합니다. 기본정렬은 시간순, 지정한 크기에 따라 개수를 설정해 조회할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin")
    public ResponseEntity<ApiRes<Page<OrderRes.AllOrderListDto>>> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size,
                                                                               @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Page<OrderRes.AllOrderListDto> list = orderService.getAllList(page, size, direction);
        return ResponseEntity.ok(ApiRes.success(list));
    }

    @Operation(
            summary = "주문 취소",
            description = "현재 로그인한 사용자의 주문을 선택해 취소합니다. 취소는 주문 후 5분 이내에만 가능합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable UUID orderId,
                                            @RequestBody OrderReq.CancelOrderDto dto) {
        User user = userDetails.getUser();
        orderService.cancelOrder(user.getUserId(), orderId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주문하기",
            description = "현재 로그인한 사용자의 장바구니 항목을 조회해 주문을 진행합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> createOrder(
            @RequestBody CreateOrderReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderRes.OrderDetailDto order = orderService.createOrder(
                request.getAddressId(),
                request.getMessage(),
                request.getDeliveryMessage(),
                userDetails.getUser());
        return ResponseEntity.ok(ApiRes.success(order));
    }

    @Operation(
            summary = "주문 내역 상세 조회",
            description = "현재 로그인한 사용자의 주문 내역 중 하나를 선택해 상세 조회합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiRes<OrderRes.OrderDetailDto>> getOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderRes.OrderDetailDto order = orderService.getOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(ApiRes.success(order));
    }

    @Operation(
            summary = "주문 내역 삭제",
            description = "현재 로그인한 사용자의 주문 내역 중 하나를 선택해 삭제합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiRes<Void>> deleteOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(ApiRes.success(null));
    }
}
