package com.delivery.user.controller;

import com.delivery.common.ApiResponse;
import com.delivery.security.UserDetailsImpl;
import com.delivery.user.dto.UserResponse;
import com.delivery.user.entity.User;
import com.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User requester = userDetails.getUser();
        User user = userService.getUserIfAccessible(requester.getUserId(), requester.getUserId());
        return new ResponseEntity<>(ApiResponse.success(UserResponse.from(user)), HttpStatus.OK);
    }
}
