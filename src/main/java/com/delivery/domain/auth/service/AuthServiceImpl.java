package com.delivery.domain.auth.service;

import com.delivery.domain.auth.dto.SignUpRequestDto;
import com.delivery.domain.auth.entity.RefreshToken;
import com.delivery.domain.auth.repository.RefreshTokenRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        if(userRepository.existsByNickname(signUpRequestDto.getNickname())){
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = new User(signUpRequestDto.getNickname(), signUpRequestDto.getEmail(), encodedPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveOrUpdateRefreshToken(Long userId, String refreshToken) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        existRefreshToken -> existRefreshToken.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(userId, refreshToken))
                );
    }

    @Override
    @Transactional
    public void updateRefreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Long userId = extractUserIdFromCookie(request);
        RefreshToken refreshToken = findValidRefreshToken(userId);
        User user = findUserById(userId);

        String newAccessToken = jwtUtil.createAccessToken(user.getUserId(), user.getNickname(), user.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getNickname(), user.getRole());

        jwtUtil.addAccessTokenToCookie(response, newAccessToken);
        refreshToken.updateToken(newRefreshToken);
    }

    private Long extractUserIdFromCookie(HttpServletRequest request) {
        String accessToken = jwtUtil.getJwtFromCookie(request);
        if (accessToken == null) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }

        return jwtUtil.getUserIdFromExpiredToken(accessToken);
    }

    private RefreshToken findValidRefreshToken(Long userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!jwtUtil.validateToken(refreshToken.getToken())) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return refreshToken;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
