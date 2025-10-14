package com.delivery.domain.user.controller;

import com.delivery.domain.user.dto.UserResponse;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.UserDetailsImpl;
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
        User user = userService.getUserById(requester.getUserId());
        return new ResponseEntity<>(ApiResponse.success(UserResponse.from(user)), HttpStatus.OK);
    }
}
