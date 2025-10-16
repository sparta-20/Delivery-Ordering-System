package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderReq;
import com.delivery.domain.order.dto.OrderRes;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Page<OrderRes.OrderListDto> getOrderList(Long userId, int page, int size, Sort.Direction direction);
    Page<OrderRes.OrderListDto> getOrdersByOwner(Long ownerUserId, int page, int size, Sort.Direction direction);
    void changeStatus(Long userId, UUID orderId, OrderReq.ChangeOrderStatusDto dto);
    void changeStatusByAdmin(Long userId, UUID orderId, OrderReq.ChangeOrderStatusDto dto);
    void rejectOrder(Long userId, UUID orderId, OrderReq.RejectOrderDto dto);
    void rejectOrderByAdmin(Long userId, UUID orderId, OrderReq.RejectOrderDto dto);
    Page<OrderRes.AllOrderListDto> getAllList(int page, int size, Sort.Direction direction);
    void cancelOrder(Long userId, UUID orderId, OrderReq.CancelOrderDto dto);
    OrderRes.OrderDetailDto getOrderDetail(Long userId, UUID orderId);
    OrderRes.OrderDetailDto getAdminOrderDetail(Long userId, UUID orderId);
    OrderRes.OrderDetailDto createOrder(UUID addressId, String message, String deliveryMessage, User user);
    OrderRes.OrderDetailDto getOrder(UUID orderId, User user);
    Order getOrderById(UUID orderId);
    void deleteOrder(UUID orderId, User user);
}
