package com.delivery.domain.auth.controller;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import com.delivery.domain.auth.service.AuthServiceImpl;
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

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
