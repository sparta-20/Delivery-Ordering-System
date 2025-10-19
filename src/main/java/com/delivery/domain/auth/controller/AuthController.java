package com.delivery.domain.auth.controller;

import com.delivery.domain.auth.dto.SignUpReq;
import com.delivery.domain.auth.service.AuthServiceImpl;
import com.delivery.global.common.ApiRes;
import com.delivery.global.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final AuthServiceImpl authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiRes<Void>> signup(@Validated @RequestBody SignUpReq signUpReq) {
        authService.signup(signUpReq);
        return ResponseEntity.ok(ApiRes.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiRes<Void>> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.updateRefreshAccessToken(request, response);
        return ResponseEntity.ok(ApiRes.success(null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiRes<Void>> logout(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response
    ) {
        authService.logout(accessToken);
        jwtUtil.expireCookie(response);
        return ResponseEntity.ok(ApiRes.success(null));
    }
}
