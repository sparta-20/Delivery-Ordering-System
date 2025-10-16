package com.delivery.domain.order.service;

import com.delivery.domain.address.entity.Address;
import com.delivery.domain.address.service.AddressService;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartItem;
import com.delivery.domain.cart.entity.CartStatusEnum;
import com.delivery.domain.cart.repository.CartItemRepository;
import com.delivery.domain.cart.repository.CartRepository;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.order.dto.OrderReq;
import com.delivery.domain.order.dto.OrderRes;
import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderMenu;
import com.delivery.domain.order.entity.OrderMenuStatusEnum;
import com.delivery.domain.order.entity.OrderStatusEnum;
import com.delivery.domain.order.repository.OrderMenuRepository;
import com.delivery.domain.order.repository.OrderRepository;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final AddressService addressService;

    @Override
    public Page<OrderRes.OrderListDto> getOrderList(Long userId,
                                                          int page,
                                                          int size,
                                                          Sort.Direction direction) {
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Order> orders = orderRepository.findByUser_UserIdAndDeletedAtIsNull(userId, pageable);
        return orders.map(OrderRes.OrderListDto::from);
    }

    @Override
    public Page<OrderRes.OrderListDto> getOrdersByOwner(Long ownerUserId,
                                                        int page,
                                                        int size,
                                                        Sort.Direction direction) {
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Order> orders = orderRepository.findByStore_Owner_UserId(ownerUserId, pageable);
        return orders.map(OrderRes.OrderListDto::from);
    }

    @Override
    @Transactional
    public void changeStatus(Long userId, UUID orderId, OrderReq.ChangeOrderStatusDto dto) {
        Order order = findOrderByOrderId(orderId);
        if (order.getStore().getOwner().getUserId().equals(userId)) order.changeStatus(dto.getStatus());
        else throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    @Override
    @Transactional
    public void rejectOrder(Long userId, UUID orderId, OrderReq.RejectOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        if (order.getStore().getOwner().getUserId().equals(userId)) order.rejectOrder(dto.getReason());
        else throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    @Override
    public Page<OrderRes.AllOrderListDto> getAllList(int page, int size, Sort.Direction direction) {
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderRes.AllOrderListDto::from);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, UUID orderId, OrderReq.CancelOrderDto dto) {
        Order order = findOrderByOrderId(orderId);
        validateOrder(order, userId);
        order.cancel(dto.getReason());
    }

    @Override
    public OrderRes.OrderDetailDto getOrderDetail(Long userId, UUID orderId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Order order = findOrderByOrderId(orderId);
        if (user.getRole().equals(UserRoleEnum.OWNER)) {
            if (!order.getStore().getOwner().getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }
        }
        return OrderRes.OrderDetailDto.from(order);
    }

    @Override
    @Transactional
    public OrderRes.OrderDetailDto createOrder(UUID addressId, String message, String deliveryMessage, User user) {
        Cart cart = cartRepository.findByUser_UserIdAndStatus(user.getUserId(), CartStatusEnum.CART)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new BusinessException(ErrorCode.CART_EMPTY);
        }

        Store store = cart.getStore();
        if (store == null) {
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }

        Address address = addressService.findById(addressId);

        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .store(store)
                .totalPrice(totalPrice)
                .address(address.getDetailAddress())
                .phoneNumber(user.getPhoneNumber())
                .message(message)
                .deliveryMessage(deliveryMessage)
                .deliveryFee(0)
                .status(OrderStatusEnum.PENDING)
                .build();

        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Menu menu = cartItem.getMenu();

            OrderMenu orderMenu = OrderMenu.builder()
                    .order(order)
                    .menu(menu)
                    .price(menu.getPrice())
                    .quantity(cartItem.getQuantity())
                    .status(OrderMenuStatusEnum.ORDER)
                    .build();

            orderMenuRepository.save(orderMenu);
            order.getOrderMenus().add(orderMenu);
        }

        cart.changeStatus(CartStatusEnum.CART_CANCEL);

        return OrderRes.OrderDetailDto.from(order);
    }

    @Override
    public OrderRes.OrderDetailDto getOrder(UUID orderId, User user) {
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return OrderRes.OrderDetailDto.from(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        order.markDeleted(user.getUserId());
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

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> {
                    log.warn("[ORDER] 주문 조회 실패 - orderId={}", orderId);
                    return new BusinessException(ErrorCode.ORDER_NOT_FOUND);
                });
    }
}