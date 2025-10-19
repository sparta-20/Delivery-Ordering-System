package com.delivery.domain.user.controller;

import com.delivery.domain.user.dto.UpdateRoleReq;
import com.delivery.domain.user.dto.UserRes;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/v1/users")
@Tag(name = "Admin")
public class UserAdminController {

    private final UserService userService;

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public ResponseEntity<ApiRes<UserRes>> updateUserRole(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "userId") Long userId,
            @Valid @RequestBody UpdateRoleReq req
    ){
        User requester = userDetails.getUser();
        UserRes userRes = userService.updateUserRole(requester.getUserId(), userId, req.getRole());
        return ResponseEntity.ok(ApiRes.success(userRes));
    }
}
