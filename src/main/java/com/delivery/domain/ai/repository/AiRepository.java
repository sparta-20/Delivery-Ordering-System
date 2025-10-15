package com.delivery.domain.ai.repository;

import com.delivery.domain.ai.dto.AiSearchRes;
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
//    /**
//     * 전체 검색 (관리자 전용)
//     */
//    @Query("""
//        SELECT new com.delivery.domain.ai.dto.AiSearchRes(
//            a.aiId, a.user.userId, a.menu.menuId, a.requestType,
//            a.prompt, a.response, a.createdAt
//        )
//        FROM Ai a
//        WHERE a.deletedAt IS NULL
//          AND (:requestType IS NULL OR a.requestType = :requestType)
//          AND (:menuId IS NULL OR a.menu.menuId = :menuId)
//        ORDER BY a.createdAt DESC
//        """)
//    Page<AiSearchRes> searchAll(@Param("requestType") RequestTypeEnum requestType,
//                                @Param("menuId") UUID menuId,
//                                Pageable pageable);
//
//    /**
//     * 특정 유저 검색 (소유자 or 관리자가 userId 명시한 경우)
//     */
//    @Query("""
//        SELECT new com.delivery.domain.ai.dto.AiSearchRes(
//            a.aiId, a.user.userId, a.menu.menuId, a.requestType,
//            a.prompt, a.response, a.createdAt
//        )
//        FROM Ai a
//        WHERE a.deletedAt IS NULL
//          AND a.user.userId = :userId
//          AND (:requestType IS NULL OR a.requestType = :requestType)
//          AND (:menuId IS NULL OR a.menu.menuId = :menuId)
//        ORDER BY a.createdAt DESC
//        """)
//    Page<AiSearchRes> searchByUserId(@Param("requestType") RequestTypeEnum requestType,
//                                     @Param("userId") Long userId,
//                                     @Param("menuId") UUID menuId,
//                                     Pageable pageable);

}