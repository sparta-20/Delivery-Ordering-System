package com.delivery.domain.menu.controller;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.dto.UpdateMenuReq;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<ApiRes<MenuRes>> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateMenuReq req
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.create(user.getUserId(), req);
        return ResponseEntity
                .created(URI.create("api/v1/menus/" + menuRes.getMenuId()))
                .body(ApiRes.success(menuRes));
    }

    //메뉴 상세 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiRes<MenuRes>> getMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "menuId") UUID menuId
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.getMenuResById(user.getUserId(), menuId);
        return ResponseEntity.ok(ApiRes.success(menuRes));
    }

    //메뉴 목록 조회
    @GetMapping
    public ResponseEntity<ApiRes<Page<MenuRes>>> getMenuList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "storeId") UUID storeId,
            @PageableDefault(size = 10, page = 0, sort = "menuId", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User user = userDetails.getUser();
        Page<MenuRes> menuRes = menuService.getMenuResListByStoreId(user.getUserId(), storeId, pageable);
        return ResponseEntity.ok(ApiRes.success(menuRes));
    }

    //메뉴 수정
    @PutMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<ApiRes<MenuRes>> updateMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "menuId") UUID menuId,
            @RequestBody UpdateMenuReq req
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.update(req, user.getUserId(), menuId);
        return ResponseEntity.ok(ApiRes.success(menuRes));
    }

    //메뉴 삭제
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<ApiRes<MenuRes>> deleteMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "menuId") UUID menuId
    ) {
        User user = userDetails.getUser();
        menuService.delete(user.getUserId(), menuId);
        return ResponseEntity.noContent().build();
    }
}
