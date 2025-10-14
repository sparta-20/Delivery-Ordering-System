package com.delivery.domain.user.controller;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.dto.UserRes;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User requester = userDetails.getUser();
        User user = userService.getUserById(requester.getUserId());
        return ResponseEntity.ok(ApiResponse.success(UserRes.from(user)));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserReq request)
    {
        User requester = userDetails.getUser();
        User user = userService.updateUser(requester.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(UserRes.from(user)));
    }

    @PostMapping("/me/password")
    public ResponseEntity<?> updateUserMePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserPasswordReq request)
    {
        User requester = userDetails.getUser();
        userService.updateUserPassword(requester.getUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User requester = userDetails.getUser();
        Long requesterUserId = requester.getUserId();
        Long targetId = requester.getUserId();
        userService.delete(requesterUserId, targetId);
        return ResponseEntity.noContent().build();
    }
}
