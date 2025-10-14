package com.delivery.global.security.filter;

import com.delivery.domain.auth.dto.LoginRequestDto;
import com.delivery.domain.auth.service.AuthService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.global.common.FilterResponseUtil;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.jwt.JwtUtil;
import com.delivery.global.security.service.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getNickname(),
                            requestDto.getPassword(),
                            Collections.emptyList()
                    );

            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        User user = userDetails.getUser();
        Long userId = userDetails.getUserId();
        String nickname = userDetails.getUsername();
        UserRoleEnum role = userDetails.getRole();

        String accessToken = jwtUtil.createAccessToken(userId, nickname, role);
        String refreshToken = jwtUtil.createRefreshToken(userId, nickname, role);

        jwtUtil.addAccessTokenToCookie(response, accessToken);
        authService.saveOrUpdateRefreshToken(user, refreshToken);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("nickname", nickname);
        data.put("role", role.getAuthority());

        FilterResponseUtil.sendSuccess(response, data);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorCode errorCode = ErrorCode.INVALID_NICKNAME;

        if (failed instanceof BadCredentialsException) {
            errorCode = ErrorCode.INVALID_PASSWORD;
        }

        FilterResponseUtil.sendError(response, errorCode);
    }
}
