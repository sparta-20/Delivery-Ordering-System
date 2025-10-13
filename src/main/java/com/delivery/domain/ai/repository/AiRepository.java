package com.delivery.domain.ai.repository;

import com.delivery.domain.ai.entity.Ai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AiRepository extends JpaRepository<Ai, UUID> {

    // 삭제되지 않은 AI 기록 조회
    Optional<Ai> findByAiIdAndDeletedAtIsNull(UUID aiId);
}