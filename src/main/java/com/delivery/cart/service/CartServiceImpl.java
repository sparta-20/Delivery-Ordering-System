package com.delivery.cart.service;

import com.delivery.cart.dto.CartItemDto;
import com.delivery.cart.dto.CartRequestDto;
import com.delivery.cart.entity.Cart;
import com.delivery.cart.entity.CartItem;
import com.delivery.cart.repository.CartItemRepository;
import com.delivery.cart.repository.CartRepository;
import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import com.delivery.user.entity.User;
import com.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart addToCart(Long userId, CartRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);

        for (CartItemDto itemDto: dto.getItems()) {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .menuId(itemDto.getMenuId())
                    .quantity(itemDto.getQuantity())
                    .build();
            cartItemRepository.save(item);
            cart.addToCart(item);
        }
        return cart;
    }
}
