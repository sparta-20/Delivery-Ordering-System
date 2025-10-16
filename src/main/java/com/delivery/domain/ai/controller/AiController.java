package com.delivery.domain.ai.controller;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.service.AiService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    /**
     * AI 설명 생성 API
     * 권한: MASTER / MANAGER / OWNER (OWNER는 본인 가게 메뉴만)
     * 응답: 201 Created + ApiRes<AiRes>
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    public ResponseEntity<ApiRes<AiRes>> createAiContent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AiCreateReq request) {

        AiRes response = aiService.createAiContent(userDetails.getUserId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiRes.success(response));
    }

    /**
     * AI 요청 기록 단건 조회 API
     * 권한: MASTER / MANAGER / OWNER (OWNER는 본인이 생성한 기록만)
     * 응답: 200 OK + ApiRes<AiRes>
     */
    @GetMapping("/{aiId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    public ResponseEntity<ApiRes<AiRes>> getAi(
            @PathVariable UUID aiId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AiRes response = aiService.getAi(userDetails.getUserId(), userDetails.getRole(), aiId);

        return ResponseEntity.ok(ApiRes.success(response));
    }

    /**
     * AI 요청 기록 논리 삭제 (Soft Delete) API
     * 권한: MASTER / MANAGER / OWNER (OWNER는 본인 기록만)
     * 응답: 204 No Content
     */
    @DeleteMapping("/{aiId}")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    public ResponseEntity<Void> deleteAi(
            @PathVariable UUID aiId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        aiService.softDelete(aiId, userDetails.getUserId(), userDetails.getRole());

        return ResponseEntity.noContent().build();
    }

    /**
     * AI 요청 기록 검색 API
     * - 기본 정렬: createdAt DESC
     * - 페이지 크기: 10, 30, 50만 허용 (기타 값은 10으로 강제)
     * - 권한: OWNER는 본인 데이터만, MANAGER/MASTER는 전체 조회
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ApiRes<Page<AiSearchRes>>> searchAiRequests(
            @RequestParam(required = false) RequestTypeEnum requestType,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UUID menuId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<AiSearchRes> result = aiService.searchAiRequests(
                requestType, userId, menuId,
                page, size, direction,
                userDetails.getUser()
        );

        return  ResponseEntity.ok(ApiRes.success(result));
    }
}