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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart addToCart(Long userId, CartRequestDto.AddCartItemDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser_UserIdAndStatus(userId, CartStatus.CART)
                        .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
        CartItem item = cartItemRepository.findByCartAndMenuId(cart, dto.getMenuId()).orElse(null);
        if (item != null) item.updateQuantity(item.getQuantity() + dto.getQuantity());
        else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .menuId(dto.getMenuId())
                    .quantity(dto.getQuantity())
                    .price(10000) // <- 임시 가격, 추후 수정 예정
                    .build();
            cartItemRepository.save(newItem);
            cart.addToCart(newItem);
        }
        return cart;
    }

    @Override
    public CartResponseDto.CartListDto getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser_UserIdAndStatus(userId, CartStatus.CART)
                .orElseGet(() -> Cart.builder().user(user).build());
        List<CartResponseDto.CartItemDetailDto> items = cart.getItems().stream()
                .map(CartResponseDto.CartItemDetailDto::from)
                .toList();
        Integer totalPrice = items.stream()
                .mapToInt(CartResponseDto.CartItemDetailDto::getTotalPrice)
                .sum();
        return CartResponseDto.CartListDto.builder()
                .cartId(cart.getCartId())
                .storeId(cart.getStoreId()) // CartItem에서 StoreId 가져오는 걸로 추후 수정
                .totalPrice(totalPrice)
                .items(items)
                .build();
    }
}
