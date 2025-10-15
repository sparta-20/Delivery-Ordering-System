package com.delivery.domain.auth.scheduler;

import com.delivery.domain.auth.repository.TokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenBlackListCleanUpScheduler  {

    private final TokenBlackListRepository tokenBlackListRepository;

    @Scheduled(cron = "0 0/30 * * * *")
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenBlackListRepository.deleteByExpiredAtBefore(now);
    }
}
