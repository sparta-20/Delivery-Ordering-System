package com.delivery.domain.menu.service.impl;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.dto.UpdateMenuReq;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.repository.MenuRepository;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
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

    /**
     * OWNER: 자기 가게 메뉴 생성 가능
     * MANAGER / MASTER: 모든 가게 메뉴 생성 가능
     */
    @Transactional
    public MenuRes create(Long userId, CreateMenuReq req) {
        User user = getUserById(userId);
        Store store = getStoreById(req.getStoreId());

        if (!canManageMenu(user.getUserId(), user, store)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }

        Menu menu = menuRepository.save(req.toEntity(store));
        return MenuRes.from(menu);
    }

    /**
     * CUSTOMER: 공개 상태 메뉴만 조회 가능
     * OWNER: 자기 가게 메뉴 상세 조회 가능 (숨김 포함)
     * MANAGER / MASTER: 전체 메뉴 상세 조회 가능
     */
    @Override
    public MenuRes findMenuResById(Long userId, UUID menuId) {
        User user = getUserById(userId);
        Menu menu = getMenuById(menuId);
        Store store = menu.getStore();

        if (!canViewMenu(userId, user, menu, store)) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }
        return MenuRes.from(menu);
    }

    /**
     * OWNER: 자기 가게 메뉴만 수정 가능
     * MANAGER / MASTER: 전체 메뉴 수정 가능
     * CUSTOMER: 불가 preAuthorize
     */
    @Override
    @Transactional
    public MenuRes update(UpdateMenuReq req, Long userId, UUID menuId) {
        User user = getUserById(userId);
        Menu menu = getMenuById(menuId);
        Store store = menu.getStore();

        if (!canManageMenu(userId, user, store)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }
        menu.update(req.getQuantity(), req.getName(), req.getPrice(), req.getStatus(), req.getDescription(), req.getImageUrl());
        return MenuRes.from(menu);
    }

    /**
     * OWNER: 자기 가게 메뉴 삭제 가능 (soft delete)
     * MANAGER / MASTER: 전체 메뉴 삭제 가능
     * CUSTOMER: 불가 preAuthorize
     */
    @Override
    @Transactional
    public MenuRes delete(Long userId, UUID menuId) {
        User user = getUserById(userId);
        Menu menu = getMenuById(menuId);
        Store store = menu.getStore();

        if (!canManageMenu(userId, user, store)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }
        menu.markDeleted(userId);
        return MenuRes.from(menu);

    }

    @Override
    public Menu getMenuById(UUID menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    private boolean canManageMenu(Long userId, User user, Store store) {
        return user.isMaster() || user.isManager() || (user.isOwner() && store.isOwnerBy(userId));
    }

    private static boolean canViewMenu(Long userId, User user, Menu menu, Store store) {
        return user.isManager() || user.isMaster() || (user.isCustomer() && !menu.isHidden()) || (user.isOwner() && store.isOwnerBy(userId));
    }

    private User getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    private Store getStoreById(UUID id) {
        return storeRepository.findByStoreIdAndStatus(id, StoreStatusEnum.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }
}