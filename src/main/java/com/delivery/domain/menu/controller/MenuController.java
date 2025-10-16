package com.delivery.domain.menu.controller;

import com.delivery.domain.menu.dto.CreateMenuReq;
import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.dto.UpdateMenuReq;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Menu", description = "메뉴 API")
@SecurityRequirement(name = "JWT")
public class MenuController {

    private final MenuService menuService;

    @Operation(
            summary = "메뉴 생성",
            description = "새로운 메뉴를 생성합니다. OWNER, MASTER, MANAGER 권한이 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = MenuRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음"
            )
    })
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

    @Operation(
            summary = "메뉴 상세 조회",
            description = "메뉴 ID로 특정 메뉴의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuRes.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메뉴를 찾을 수 없음"
            )
    })
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiRes<MenuRes>> getMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "메뉴 ID", required = true)
            @PathVariable(value = "menuId") UUID menuId
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.findMenuResById(user.getUserId(), menuId);
        return ResponseEntity.ok(ApiRes.success(menuRes));
    }

    @Operation(
            summary = "메뉴 수정",
            description = "기존 메뉴의 정보를 수정합니다. OWNER, MASTER, MANAGER 권한이 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = MenuRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메뉴를 찾을 수 없음"
            )
    })
    @PutMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<ApiRes<MenuRes>> updateMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "메뉴 ID", required = true)
            @PathVariable(value = "menuId") UUID menuId,
            @RequestBody UpdateMenuReq req
    ) {
        User user = userDetails.getUser();
        MenuRes menuRes = menuService.update(req, user.getUserId(), menuId);
        return ResponseEntity.ok(ApiRes.success(menuRes));
    }

    @Operation(
            summary = "메뉴 삭제",
            description = "메뉴를 삭제합니다. OWNER, MASTER, MANAGER 권한이 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메뉴를 찾을 수 없음"
            )
    })
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<ApiRes<MenuRes>> deleteMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "메뉴 ID", required = true)
            @PathVariable(value = "menuId") UUID menuId
    ) {
        User user = userDetails.getUser();
        menuService.delete(user.getUserId(), menuId);
        return ResponseEntity.noContent().build();
    }

    //메뉴 목록 조회

}
