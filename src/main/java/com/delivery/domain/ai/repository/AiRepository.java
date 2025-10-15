package com.delivery.domain.ai.repository;

import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AiRepository extends JpaRepository<Ai, UUID> {

    // 삭제되지 않은 AI 기록 조회
    Optional<Ai> findByAiIdAndDeletedAtIsNull(UUID aiId);

     // 전체 검색 (모든 조건)
     @Query("SELECT a FROM Ai a " +
             "JOIN FETCH a.user u " +
             "JOIN FETCH a.menu m " +
             "WHERE (:requestType IS NULL OR a.requestType = :requestType) " +
             "AND (:userId IS NULL OR u.userId = :userId) " +
             "AND (:menuId IS NULL OR m.menuId = :menuId) " +
             "AND a.deletedAt IS NULL " +
             "AND u.deletedAt IS NULL " +
             "AND m.deletedAt IS NULL")
     Page<Ai> searchAiRequests(
             @Param("requestType") RequestTypeEnum requestType,
             @Param("userId") Long userId,
             @Param("menuId") UUID menuId,
             Pageable pageable
     );
}