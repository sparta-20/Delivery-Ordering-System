package com.delivery.domain.order.service;

import com.delivery.domain.order.dto.OrderRequestDto;
import com.delivery.domain.order.dto.OrderResponseDto;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.repository.OrderRepository;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public List<OrderResponseDto.OrderListDto> getOrderList(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    public List<OrderResponseDto.OrderListDto> getOrdersByOwner(Long ownerId) {
        // 수정 필요
        List<Order> orders = orderRepository.findByOwnerId(ownerId);
        return orders.stream()
                .map(OrderResponseDto.OrderListDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeStatus(Long userId, UUID orderId, OrderRequestDto.ChangeOrderStatusDto dto) {
        Order order = findOrderByOrderId(orderId);
        // TODO: orderId 이용해서 Store 정보 -> 가게 주인 확인 후 현재 로그인한 유저랑 일치하는지 확인
        if (order.getUser().getRole().equals(UserRoleEnum.OWNER)) validateOwner(userId, dto.getOwnerId());
        order.changeStatus(dto.getStatus());
    }

    @Override
    @Transactional
    public void rejectOrder(Long userId, UUID orderId, OrderRequestDto.RejectOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        // TODO
        if (order.getUser().getRole().equals(UserRoleEnum.OWNER)) validateOwner(userId, dto.getOwnerId());
        order.rejectOrder(dto.getReason());
    }

    private void validateOwner(Long userId, Long ownerId) {
        if (!userId.equals(ownerId)) throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    private Order findOrderByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                // FIXME: 에러코드 수정
                .orElseThrow(() -> new BusinessException(ErrorCode.AI_REQUEST_NOT_FOUND));
    }
}
