package com.delivery.domain.menu.controller;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<?> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateMenuReq req
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.create(user.getUserId(), req);
        return ResponseEntity
                .created(URI.create("api/v1/menus/" + menuRes.getMenuId()))
                .body(ApiResponse.success(menuRes));
    }

    //메뉴 상세 조회
    @PostMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<?> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "menuId") UUID menuId
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.getMenuResById(user.getUserId(), menuId);
        return ResponseEntity.ok(ApiResponse.success(menuRes));
    }

    //메뉴 목록 조회

    //메뉴 수정

    //메뉴 삭제
}
