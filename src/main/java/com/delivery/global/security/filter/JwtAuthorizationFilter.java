package com.delivery.global.security.filter;

import com.delivery.domain.auth.service.AuthService;
import com.delivery.global.common.FilterResponseUtil;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.jwt.JwtUtil;
import com.delivery.global.security.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getJwtFromCookie(request);

        if (StringUtils.hasText(token)) {
            try {
                if (authService.isBlacklisted(token)) {
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }

                boolean isValid = jwtUtil.validateToken(token);
                if (!isValid) {
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }

                Claims userInfo = jwtUtil.getUserInfoFromToken(token);
                setAuthentication(Long.valueOf(userInfo.getSubject()));

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                FilterResponseUtil.sendError(response, ErrorCode.INVALID_TOKEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(Long userId) {
        UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
