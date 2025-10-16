package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderReq;
import com.delivery.domain.order.dto.OrderRes;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderRes.OrderListDto> getOrderList(Long userId);
    List<OrderRes.OrderListDto> getOrdersByOwner(Long ownerUserId);
    void changeStatus(Long userId, UUID orderId, OrderReq.ChangeOrderStatusDto dto);
    void rejectOrder(Long userId, UUID orderId, OrderReq.RejectOrderDto dto);
    List<OrderRes.AllOrderListDto> getAllList();
    void cancelOrder(Long userId, UUID orderId, OrderReq.CancelOrderDto dto);
    OrderRes.OrderDetailDto getOrderDetail(Long userId, UUID orderId);
    OrderRes.OrderDetailDto createOrder(UUID addressId, String message, String deliveryMessage, User user);
    OrderRes.OrderDetailDto getOrder(UUID orderId, User user);
    Order getOrderById(UUID orderId);
    void deleteOrder(UUID orderId, User user);
}
