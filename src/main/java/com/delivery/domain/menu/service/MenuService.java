package com.delivery.domain.menu.service;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.entity.Menu;

import java.util.UUID;

public interface MenuService {
    MenuRes create(Long userId, CreateMenuReq req);
    MenuRes getMenuResById(Long userId, UUID menuId);
    Menu getMenuById(UUID menuId);
}
