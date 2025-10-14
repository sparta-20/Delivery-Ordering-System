package com.delivery.domain.menu.service.impl;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MenuServiceImplTest {

    @Autowired
    private MenuService menuService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private StoreRepository storeRepository;
    @MockitoBean
    private MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void 메뉴를_생성할_수_있다() {
        long userId = 1L;
        UUID storeId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();
        User user = User.builder().userId(userId).build();
        Store store = Store.builder().storeId(storeId).status(StoreStatusEnum.ACTIVE).owner(user).build();
        Menu menu = Menu.builder().menuId(menuId).status(MenuStatusEnum.AVAILABLE).store(store).build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(store));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(storeId);
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menuId, menuRes.getMenuId());
        assertEquals(storeId, menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }

    @Test
    @DisplayName("CUSTOMER는 메뉴 생성할 때 나의 가게가 아닌경우 에외 발생")
    void 메뉴_생성할_때_나의_가게가_아닌경우_에외_발생() {
        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        long userId = 1L;
        User user = User.builder().role(role).userId(userId).build();
        User otherStoreOwner = User.builder().userId(2L).build();

        UUID storeId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();
        Store otherStore = Store.builder().storeId(storeId).status(StoreStatusEnum.ACTIVE).owner(otherStoreOwner).build();
        Menu menu = Menu.builder().menuId(menuId).status(MenuStatusEnum.AVAILABLE).store(otherStore).build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            CreateMenuReq req = new CreateMenuReq();
            req.setStoreId(storeId);
            menuService.create(userId, req);
        });

        assertEquals(ErrorCode.FORBIDDEN_READ_STORE, businessException.getErrorCode());
    }

    @Test
    @DisplayName("MASTER는 모든 Store의 메뉴를 생성할 수 있다.")
    void MASTER는_모든_Store의_메뉴를_생성할_수_있다() {
        UserRoleEnum role = UserRoleEnum.MASTER;
        long userId = 1L;
        User user = User.builder().role(role).userId(userId).build();
        User otherStoreOwner = User.builder().userId(2L).build();

        UUID storeId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();
        Store otherStore = Store.builder().storeId(storeId).status(StoreStatusEnum.ACTIVE).owner(otherStoreOwner).build();
        Menu menu = Menu.builder().menuId(menuId).status(MenuStatusEnum.AVAILABLE).store(otherStore).build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(storeId);
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menuId, menuRes.getMenuId());
        assertEquals(storeId, menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }
    @Test
    @DisplayName("MANAGER는 모든 Store의 메뉴를 생성할 수 있다.")
    void MANAGER는_모든_Store의_메뉴를_생성할_수_있다() {
        UserRoleEnum role = UserRoleEnum.MASTER;
        long userId = 1L;
        User user = User.builder().role(role).userId(userId).build();
        User otherStoreOwner = User.builder().userId(2L).build();

        UUID storeId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();
        Store otherStore = Store.builder().storeId(storeId).status(StoreStatusEnum.ACTIVE).owner(otherStoreOwner).build();
        Menu menu = Menu.builder().menuId(menuId).status(MenuStatusEnum.AVAILABLE).store(otherStore).build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(storeId);
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menuId, menuRes.getMenuId());
        assertEquals(storeId, menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }

}