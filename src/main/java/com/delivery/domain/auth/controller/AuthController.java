package com.delivery.domain.auth.controller;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import com.delivery.domain.auth.service.AuthServiceImpl;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.updateRefreshAccessToken(request, response);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response
    ) {
        authService.logout(accessToken);
        jwtUtil.expireCookie(response);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
