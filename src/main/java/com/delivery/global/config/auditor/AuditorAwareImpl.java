package com.delivery.global.config.auditor;

import com.delivery.global.security.service.UserDetailsImpl;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication) {
            return null;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

        return Optional.of(userDetails.getUserId());
    }
}