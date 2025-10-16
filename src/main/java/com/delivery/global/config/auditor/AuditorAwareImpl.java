package com.delivery.global.config.auditor;

import com.delivery.global.security.service.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication) {
            return null;
        }

        // 익명 사용자인 경우 (Principal이 String "anonymousUser")
        if (authentication.getPrincipal() instanceof String) {
            return Optional.empty();
        }

        // Principal이 UserDetailsImpl인 경우에만 캐스팅
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getUserId());
        }

        return Optional.empty();
    }
}
