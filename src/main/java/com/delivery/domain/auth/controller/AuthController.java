package com.delivery.domain.auth.controller;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import com.delivery.domain.auth.service.AuthService;
import com.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
