package com.delivery.global.security;

import com.delivery.domain.auth.dto.LoginRequestDto;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public JwtAuthenticationFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getNickname(),
                            requestDto.getPassword(),
                            Collections.emptyList()
                    );

            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        Long userId = userDetails.getUserId();
        String nickname = userDetails.getUsername();
        UserRoleEnum role = userDetails.getRole();

        String accessToken = jwtUtil.createToken(userId, nickname, role);

        addAccessTokenToCookie(response, accessToken);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("nickname", nickname);
        data.put("role", role.getAuthority());

        ApiResponse<?> apiResponse = ApiResponse.success(data);
        writeResponse(response, apiResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ApiResponse<?> apiResponse = ApiResponse.error(ErrorCode.INVALID_CREDENTIALS.getCode(), ErrorCode.INVALID_CREDENTIALS.getMessage());

        writeResponse(response, apiResponse);
    }

    private void writeResponse(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    private void addAccessTokenToCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(30 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
