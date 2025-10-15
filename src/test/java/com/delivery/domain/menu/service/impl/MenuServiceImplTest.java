package com.delivery.domain.menu.service.impl;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.dto.UpdateMenuReq;
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
    @DisplayName("OWNBER는 자신의 가게에 메뉴를 생성할 수 있다.")
    void 메뉴를_생성할_수_있다() {
        long userId = 1L;
        User owner = createUser(userId, UserRoleEnum.OWNER);
        Store ownerStore = createStore(owner);
        Menu menu = createMenu(ownerStore, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(owner);
        when(storeRepository.findByStoreIdAndStatus(ownerStore.getStoreId(), StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(ownerStore));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(ownerStore.getStoreId());
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menu.getMenuId(), menuRes.getMenuId());
        assertEquals(ownerStore.getStoreId(), menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }

    @Test
    @DisplayName("OWNER는 메뉴 생성할 때 나의 가게가 아닌 경우 에외 발생")
    void 메뉴_생성할_때_나의_가게가_아닌경우_에외_발생() {
        UserRoleEnum role = UserRoleEnum.OWNER;
        long userId = 1L;
        long otherUser = 2L;
        User user = createUser(userId, role);
        User otherStoreOwner = createUser(otherUser, role);
        Store otherStore = createStore(otherStoreOwner);
        Menu menu = createMenu(otherStore, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(otherStore.getStoreId(), StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            CreateMenuReq req = new CreateMenuReq();
            req.setStoreId(otherStore.getStoreId());
            menuService.create(userId, req);
        });

        assertEquals(ErrorCode.FORBIDDEN_READ_STORE, businessException.getErrorCode());
    }

    @Test
    @DisplayName("MASTER는 모든 Store의 메뉴를 생성할 수 있다.")
    void MASTER는_모든_Store의_메뉴를_생성할_수_있다() {
        UserRoleEnum role = UserRoleEnum.MASTER;
        long userId = 1L; long otherStoreUserId = 2L;
        User user = createUser(userId, role);
        Store otherStore = createStore(createUser(otherStoreUserId, UserRoleEnum.OWNER));
        Menu menu = createMenu(otherStore, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(otherStore.getStoreId(), StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(otherStore.getStoreId());
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menu.getMenuId(), menuRes.getMenuId());
        assertEquals(otherStore.getStoreId(), menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }

    @Test
    @DisplayName("MANAGER는 모든 Store의 메뉴를 생성할 수 있다.")
    void MANAGER는_모든_Store의_메뉴를_생성할_수_있다() {
        UserRoleEnum role = UserRoleEnum.MASTER;
        long userId = 1L; long otherUserId = 2L;
        User user = createUser(userId, role);
        Store otherStore = createStore(createUser(otherUserId, UserRoleEnum.OWNER));
        Menu menu = createMenu(otherStore, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(user);
        when(storeRepository.findByStoreIdAndStatus(otherStore.getStoreId(), StoreStatusEnum.ACTIVE)).thenReturn(Optional.of(otherStore));
        when(menuRepository.save(any())).thenReturn(menu);

        CreateMenuReq req = new CreateMenuReq();
        req.setStoreId(otherStore.getStoreId());
        MenuRes menuRes = menuService.create(userId, req);

        assertEquals(menu.getMenuId(), menuRes.getMenuId());
        assertEquals(otherStore.getStoreId(), menuRes.getStoreId());
        assertEquals(MenuStatusEnum.AVAILABLE, menuRes.getStatus());
    }

    @Test
    @DisplayName("CUSTOMER는 숨김(HIDDEN) 메뉴를 상세 조회하면 예외 반환")
    void customerCannotViewHiddenMenu() {
        long userId = 1L; long ownerId = 2L;
        User customer = createUser(userId, UserRoleEnum.CUSTOMER);

        User owner = createUser(ownerId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu hiddenMenu = createMenu(store, MenuStatusEnum.HIDDEN);

        when(userService.getUserById(userId)).thenReturn(customer);
        when(menuRepository.findById(hiddenMenu.getMenuId())).thenReturn(Optional.of(hiddenMenu));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                menuService.findMenuResById(userId, hiddenMenu.getMenuId())
        );
        assertEquals(ErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("OWNER는 자신의 가게 숨김 메뉴 상세 조회할 수 있다.")
    void ownerCanViewHiddenMenu() {
        long userId = 1L;
        User owner = createUser(userId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu hiddenMenu = createMenu(store, MenuStatusEnum.HIDDEN);

        when(userService.getUserById(userId)).thenReturn(owner);
        when(menuRepository.findById(hiddenMenu.getMenuId())).thenReturn(Optional.of(hiddenMenu));

        MenuRes menuRes = menuService.findMenuResById(userId, hiddenMenu.getMenuId());

        assertEquals(hiddenMenu.getMenuId(), menuRes.getMenuId());
        assertEquals(store.getStoreId(), menuRes.getStoreId());
    }


    @Test
    @DisplayName("CUSTOMER는 메뉴 수정시 예외 발생")
    void customerCannotUpdateMenu() {
        long userId = 1L; long ownerId = 2L;
        User customer = createUser(userId, UserRoleEnum.CUSTOMER);
        User owner = createUser(ownerId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu menu = createMenu(store, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(customer);
        when(menuRepository.findById(menu.getMenuId())).thenReturn(Optional.of(menu));

        UpdateMenuReq req = new UpdateMenuReq();
        req.setName("변경");

        BusinessException exception = assertThrows(BusinessException.class, () ->
                menuService.update(req, userId, menu.getMenuId())
        );
        assertEquals(ErrorCode.FORBIDDEN_READ_STORE, exception.getErrorCode());
    }

    @Test
    @DisplayName("OWNER는 자기 가게 메뉴 수정할 수 있다.")
    void ownerCanUpdateOwnMenu() {
        long userId = 1L;
        User owner = createUser(userId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu menu = createMenu(store, MenuStatusEnum.AVAILABLE);

        when(userService.getUserById(userId)).thenReturn(owner);
        when(menuRepository.findById(menu.getMenuId())).thenReturn(Optional.of(menu));

        UpdateMenuReq req = new UpdateMenuReq();
        req.setName("변경");

        MenuRes updated = menuService.update(req, userId, menu.getMenuId());

        assertEquals("변경", updated.getName());
        assertEquals(menu.getMenuId(), updated.getMenuId());
    }

    @Test
    @DisplayName("OWNER는 자기 가게 숨김 메뉴 조회할 수 있다.")
    void ownerCanViewOwnMenu() {
        long userId = 1L;
        User owner = createUser(userId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu hiddenMenu = createMenu(store, MenuStatusEnum.HIDDEN);

        when(userService.getUserById(userId)).thenReturn(owner);
        when(menuRepository.findById(hiddenMenu.getMenuId())).thenReturn(Optional.of(hiddenMenu));

        MenuRes result = menuService.findMenuResById(userId, hiddenMenu.getMenuId());

        assertEquals(hiddenMenu.getMenuId(), result.getMenuId());
    }

    @Test
    @DisplayName("MANAGER는 숨김 메뉴 조회할 수 있다.")
    void managerViewAllMenus() {
        long userId = 1L; long ownerId = 2L;
        User manager = createUser(userId, UserRoleEnum.MANAGER);
        User owner = createUser(ownerId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu hiddenMenu = createMenu(store, MenuStatusEnum.HIDDEN);

        when(userService.getUserById(userId)).thenReturn(manager);
        when(menuRepository.findById(hiddenMenu.getMenuId())).thenReturn(Optional.of(hiddenMenu));

        MenuRes result = menuService.findMenuResById(userId, hiddenMenu.getMenuId());

        assertEquals(hiddenMenu.getMenuId(), result.getMenuId());
    }

    @Test
    @DisplayName("MASTER는 숨김 메뉴 조회할 수 있다.")
    void masterCanViewAllMenus() {
        long userId = 1L; long ownerId = 2L;
        User manager = createUser(userId, UserRoleEnum.MASTER);
        User owner = createUser(ownerId, UserRoleEnum.OWNER);
        Store store = createStore(owner);
        Menu hiddenMenu = createMenu(store, MenuStatusEnum.HIDDEN);

        when(userService.getUserById(userId)).thenReturn(manager);
        when(menuRepository.findById(hiddenMenu.getMenuId())).thenReturn(Optional.of(hiddenMenu));

        MenuRes result = menuService.findMenuResById(userId, hiddenMenu.getMenuId());

        assertEquals(hiddenMenu.getMenuId(), result.getMenuId());
    }

    private static User createUser(long userId, UserRoleEnum role) {
        return User.builder().userId(userId).role(role).build();
    }

    private static Menu createMenu(Store store, MenuStatusEnum menuStatusEnum) {
        UUID menuId = UUID.randomUUID();
        return Menu.builder().menuId(menuId).store(store).status(menuStatusEnum).build();
    }

    private static Store createStore(User owner) {
        UUID storeId = UUID.randomUUID();
        return Store.builder().storeId(storeId).owner(owner).status(StoreStatusEnum.ACTIVE).build();
    }
}