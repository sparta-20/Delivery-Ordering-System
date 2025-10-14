package com.delivery.domain.menu.service;

import com.delivery.domain.menu.entity.Menu;

import java.util.UUID;

public interface MenuService {
    Menu getMenuById(UUID menuId);
}