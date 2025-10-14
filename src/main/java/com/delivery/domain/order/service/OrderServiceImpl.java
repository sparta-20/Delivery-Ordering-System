package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderRequestDto;
import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderStatusEnum;
import com.delivery.domain.order.repository.OrderRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderResponseDto.OrderListDto> getOrderList(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    public List<OrderResponseDto.OrderListDto> getOrdersByOwner(Long ownerUserId) {
        List<Order> orders = orderRepository.findByStore_Owner_UserId(ownerUserId);
        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeStatus(Long userId, UUID orderId, OrderRequestDto.ChangeOrderStatusDto dto) {
        Order order = findOrderByOrderId(orderId);
        if (order.getStore().getOwner().getUserId().equals(userId)) order.changeStatus(dto.getStatus());
        else throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    @Override
    @Transactional
    public void rejectOrder(Long userId, UUID orderId, OrderRequestDto.RejectOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        if (order.getStore().getOwner().getUserId().equals(userId)) order.rejectOrder(dto.getReason());
        else throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    @Override
    public List<OrderResponseDto.AllOrderListDto> getAllList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseDto.AllOrderListDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, UUID orderId, OrderRequestDto.CancelOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        validateOrder(order, userId);
        order.cancel(dto.getReason());
    }

    @Override
    public OrderResponseDto.OrderDetailDto getOrderDetail(Long userId, UUID orderId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Order order = findOrderByOrderId(orderId);
        if (user.getRole().equals(UserRoleEnum.OWNER)) {
            if (!order.getStore().getOwner().getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }
        }
        int recentOrderCount = calculateOrder(user, order);
        return OrderResponseDto.OrderDetailDto.from(order, recentOrderCount);
    }

    private int calculateOrder(User user, Order order) {
        LocalDateTime six = LocalDateTime.now().minusMonths(6);
        return orderRepository.countByUserAndStoreSince(
                user.getUserId(),
                order.getStore().getStoreId(),
                six
        );
    }

    private void validateOrder(Order order, Long userId) {
        if (!order.getUser().getUserId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);
        if (order.getStatus() != OrderStatusEnum.PENDING)
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        if (Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5) {
            throw new BusinessException(ErrorCode.ORDER_CANCEL_TIME_EXCEEDED);
        }
    }

    private Order findOrderByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }
}
