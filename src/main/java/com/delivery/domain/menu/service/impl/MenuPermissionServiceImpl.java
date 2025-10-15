package com.delivery.domain.menu.service.impl;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.menu.service.MenuPermissionService;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.delivery.domain.menu.entity.MenuStatusEnum.*;

@Service
public class MenuPermissionServiceImpl implements MenuPermissionService {
    // 상수 리스트 재사용
    private static final List<MenuStatusEnum> CUSTOMER_STATUSES = List.of(AVAILABLE);
    private static final List<MenuStatusEnum> MANAGER_STATUSES = List.of(AVAILABLE, HIDDEN, SOLD_OUT);

    public boolean canManageMenu(User user, Store store) {
        return isAdmin(user) || store.isOwnerBy(user.getUserId());
    }

    public boolean canReadMenuDetail(User user, Menu menu, Store store) {
        return isAdmin(user) || (user.isCustomer() && !menu.isHidden()) || store.isOwnerBy(user.getUserId());
    }

    private static boolean isAdmin(User user) {
        return user.isManager() || user.isMaster();
    }

    public List<MenuStatusEnum> getAccessibleStatuses(User user, Store store) {
        if(user.isCustomer()) return CUSTOMER_STATUSES;
        if(canManageMenu(user, store)) return MANAGER_STATUSES;
        return CUSTOMER_STATUSES;
    }
}
