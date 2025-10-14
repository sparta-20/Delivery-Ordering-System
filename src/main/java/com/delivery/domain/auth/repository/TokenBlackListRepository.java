package com.delivery.domain.auth.repository;

import com.delivery.domain.auth.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TokenBlackListRepository extends JpaRepository<TokenBlacklist, UUID> {
    boolean existsByAccessToken(String accessToken);
    void deleteByExpiredAtBefore(LocalDateTime now);
}
