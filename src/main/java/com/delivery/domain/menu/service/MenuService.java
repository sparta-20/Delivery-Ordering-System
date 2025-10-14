package com.delivery.domain.menu.service;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;

public interface MenuService {
    MenuRes create(Long userId, CreateMenuReq req);
}
