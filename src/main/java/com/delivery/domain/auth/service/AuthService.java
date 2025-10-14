package com.delivery.domain.auth.service;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signup(SignUpRequestDto signUpRequestDto);
    void saveOrUpdateRefreshToken(Long userId, String refreshToken);
    void updateRefreshAccessToken(HttpServletRequest request, HttpServletResponse response);
}
