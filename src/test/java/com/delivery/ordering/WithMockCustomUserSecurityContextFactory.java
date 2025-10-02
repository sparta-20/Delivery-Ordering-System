package com.delivery.ordering;

import com.delivery.security.UserDetailsImpl;
import com.delivery.user.entity.PublicStatus;
import com.delivery.user.entity.User;
import com.delivery.user.entity.UserRoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        // User 객체 생성
        User user = User.builder()
                .userId(annotation.id())
                .nickname(annotation.nickname())
                .email(annotation.email())
                .role(UserRoleEnum.valueOf(annotation.role()))
                .publicStatus(PublicStatus.PUBLIC)
                .build();
        
        // UserDetailsImpl 생성
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        
        // Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + annotation.role()))
        );
        
        context.setAuthentication(auth);
        return context;
    }
}
