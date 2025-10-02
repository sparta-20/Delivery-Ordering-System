package com.delivery.cart.service;

import com.delivery.cart.dto.CartRequestDto;
import com.delivery.cart.dto.CartResponseDto;
import com.delivery.cart.entity.Cart;
import com.delivery.cart.entity.CartItem;
import com.delivery.cart.entity.CartStatus;
import com.delivery.cart.repository.CartItemRepository;
import com.delivery.cart.repository.CartRepository;
import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import com.delivery.user.entity.User;
import com.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart addToCart(Long userId, CartRequestDto.AddCartItemDto dto) {
        User user = findUserById(userId);
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findByCartAndMenuId(cart, dto.getMenuId()).orElse(null);
        if (item != null) item.updateQuantity(dto.getQuantity());
        else addCartItem(cart, dto);

        return cart;
    }

    @Override
    public CartResponseDto.CartListDto getCart(Long userId) {
        User user = findUserById(userId);
        Cart cart = getOrCreateCart(user);
        List<CartResponseDto.CartItemDetailDto> items = toItemDto(cart.getItems());
        int totalPrice = calculatePrice(cart.getItems());
        return CartResponseDto.CartListDto.builder()
                .cartId(cart.getCartId())
                .storeId(cart.getStoreId()) // CartItem에서 StoreId 가져오는 걸로 추후 수정
                .totalPrice(totalPrice)
                .items(items)
                .build();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser_UserIdAndStatus(user.getUserId(), CartStatus.CART)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    private void addCartItem(Cart cart, CartRequestDto.AddCartItemDto dto) {
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .menuId(dto.getMenuId())
                .quantity(dto.getQuantity())
                .price(10000) // <- 임시 가격, 추후 수정 예정
                .build();
        cartItemRepository.save(newItem);
        cart.addToCart(newItem);
    }

    private List<CartResponseDto.CartItemDetailDto> toItemDto(List<CartItem> items) {
        return items.stream()
                .map(item -> CartResponseDto.CartItemDetailDto.builder()
                        .cartItemId(item.getCartMenuId())
                        .menuId(item.getMenuId())
                        .menuName("메뉴 이름: " + item.getMenuId()) // 수정 필요
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getPrice() * item.getQuantity())
                        .build())
                .toList();
    }

    private Integer calculatePrice(List<CartItem> items) {
        return items.stream()
                .mapToInt(item -> item.getQuantity() * item.getPrice())
                .sum();
    }
}
