package com.delivery.domain.menu.service;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.user.entity.User;

import java.util.List;

public interface MenuPermissionService {

    // 생성,수정,삭제 할 수 권한이 있는 지
    boolean canManageMenu(User user, Store store);
    // user가 menu를 조회 할 수 있는 지
    boolean canReadMenuDetail(User user, Menu menu, Store store);
    // user가 menu의
    List<MenuStatusEnum> getAccessibleStatuses(User user, Store store);
}
