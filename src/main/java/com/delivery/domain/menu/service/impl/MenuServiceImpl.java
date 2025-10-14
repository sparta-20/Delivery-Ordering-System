package com.delivery.domain.menu.service.impl;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.repository.MenuRepository;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final UserService userService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuRes create(Long userId, CreateMenuReq req) {
        User user = getUserById(userId);
        Store store = getStoreById(req.getStoreId());
        if(UserRoleEnum.CUSTOMER.equals(user.getRole()))
            checkStoreOwner(store, user);
        Menu menu = menuRepository.save(createMenu(req, store));
        return MenuRes.from(menu);
    }

    private Menu createMenu(CreateMenuReq req, Store store) {
        return Menu.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .status(req.getStatus())
                .store(store).build();
    }

    private void checkStoreOwner(Store store, User user) {
        if (!user.getUserId().equals(store.getOwner().getUserId()))
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
    }

    private User getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    private Store getStoreById(UUID id) {
        return storeRepository.findByStoreIdAndStatus(id, StoreStatusEnum.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }
}