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

    @Query("""
        SELECT a FROM Ai a
        JOIN FETCH a.user
        join FETCH a.menu
        WHERE a.aiId = :aiId AND a.deletedAt IS NULL
        """)
    Optional<Ai> findDetail(UUID aiId);

     // 전체 검색 (모든 조건)
     @Query("""
        SELECT a FROM Ai a
        JOIN FETCH a.user u
        JOIN FETCH a.menu m
        WHERE (:userId IS NULL OR u.userId = :userId)
          AND (:requestType IS NULL OR a.requestType = :requestType)
          AND (:menuId IS NULL OR m.menuId = :menuId)
          AND a.deletedAt IS NULL
        """)
     Page<Ai> searchAiRequests(
             @Param("userId") Long userId,
             @Param("requestType") RequestTypeEnum requestType,
             @Param("menuId") UUID menuId,
             Pageable pageable
     );

    @Query("""
        SELECT a FROM Ai a
        JOIN FETCH a.user
        JOIN FETCH a.menu
        WHERE a.aiId = :aiId
          AND a.user.userId = :userId
          AND a.deletedAt IS NULL
        """)
    Optional<Ai> findDetailByUserId(UUID aiId, Long userId);

    // 본인 소유의 AI 요청 단건 조회
    Optional<Ai> findByAiIdAndUser_UserIdAndDeletedAtIsNull(UUID aiId, Long userId);
}