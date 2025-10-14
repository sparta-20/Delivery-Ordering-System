package com.delivery.domain.auth.service;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import com.delivery.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signup(SignUpRequestDto signUpRequestDto);
    void saveOrUpdateRefreshToken(User user, String refreshToken);
    void updateRefreshAccessToken(HttpServletRequest request, HttpServletResponse response);
    void logout(String accessToken);
    boolean isBlacklisted(String accessToken);
}
