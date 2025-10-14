package com.delivery.domain.cart.service;

import com.delivery.domain.cart.dto.CartRequestDto;
import com.delivery.domain.cart.dto.CartResponseDto;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartItem;
import com.delivery.domain.cart.entity.CartStatusEnum;
import com.delivery.domain.cart.repository.CartItemRepository;
import com.delivery.domain.cart.repository.CartRepository;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.repository.MenuRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public Cart addToCart(Long userId, CartRequestDto.AddCartItemDto dto) {
        User user = findUserById(userId);
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        Cart cart = getOrCreateCart(user, menu);
        CartItem item = cartItemRepository.findByCartAndMenu(cart, menu).orElse(null);
        if (item != null) item.updateQuantity(dto.getQuantity() + item.getQuantity());
        else addCartItem(cart, menu, dto.getQuantity());

        return cart;
    }

    @Override
    public CartResponseDto.CartListDto getCart(Long userId) {
        User user = findUserById(userId);
        Cart cart = getExistingCart(user);
        List<CartResponseDto.CartItemDetailDto> items = toItemDto(cart.getItems());
        int totalPrice = calculatePrice(cart.getItems());
        return CartResponseDto.CartListDto.builder()
                .cartId(cart.getCartId())
                .storeId(cart.getStore().getStoreId())
                .totalPrice(totalPrice)
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUser_UserIdAndStatus(userId, CartStatusEnum.CART)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
        cart.clearCart();
    }

    @Override
    @Transactional
    public void updateCartItem(Long userId, UUID itemId, Integer quantity) {
        CartItem item = findCartItem(itemId, userId);
        item.updateQuantity(quantity);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private Cart getOrCreateCart(User user, Menu menu) {
        return cartRepository.findByUser_UserIdAndStatus(user.getUserId(), CartStatusEnum.CART)
                .map(cart -> {
                    if (!cart.getStore().equals(menu.getStore()))
                        throw new BusinessException(ErrorCode.DIFFERENT_STORE);
                    return cart;
                })
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).store(menu.getStore()).build()));
    }

    private Cart getExistingCart(User user) {
        return cartRepository.findByUser_UserIdAndStatus(user.getUserId(), CartStatusEnum.CART)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
    }

    private void addCartItem(Cart cart, Menu menu, Integer quantity) {
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .menu(menu)
                .quantity(quantity)
                .build();
        cartItemRepository.save(newItem);
        cart.addToCart(newItem);
    }

    private List<CartResponseDto.CartItemDetailDto> toItemDto(List<CartItem> items) {
        return items.stream()
                .map(item -> CartResponseDto.CartItemDetailDto.builder()
                        .cartItemId(item.getCartMenuId())
                        .menuId(item.getMenu().getMenuId())
                        .menuName(item.getMenu().getName())
                        .quantity(item.getQuantity())
                        .price(item.getMenu().getPrice())
                        .totalPrice(item.getMenu().getPrice() * item.getQuantity())
                        .build())
                .toList();
    }

    private Integer calculatePrice(List<CartItem> items) {
        return items.stream()
                .mapToInt(item -> item.getQuantity() * item.getMenu().getPrice())
                .sum();
    }

    private CartItem findCartItem(UUID cartItemId, Long userId) {
        return cartItemRepository.findByCartMenuIdAndCart_User_UserId(cartItemId, userId).orElseThrow(() -> new BusinessException(ErrorCode.ITEM_REQUEST_NOT_FOUND));
    }
}
