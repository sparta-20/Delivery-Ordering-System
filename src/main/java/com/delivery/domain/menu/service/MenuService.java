package com.delivery.domain.menu.service;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.dto.UpdateMenuReq;
import com.delivery.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuService {
    MenuRes create(Long userId, CreateMenuReq req);
    MenuRes getMenuResById(Long userId, UUID menuId);
    Menu getMenuById(UUID menuId);
    MenuRes update(UpdateMenuReq req, Long userId, UUID menuId);
    MenuRes delete(Long userId, UUID menuId);
    Page<MenuRes> getMenuResListByStoreId(Long userId, UUID storeId, Pageable pageable);
}
