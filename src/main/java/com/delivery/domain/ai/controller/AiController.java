package com.delivery.domain.ai.controller;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.service.AiService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * 응답: 201 Created + ApiResponse<AiRes>
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    public ResponseEntity<ApiResponse<AiRes>> createAiContent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AiCreateReq request) {

        AiRes response = aiService.createAiContent(userDetails.getUserId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
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
}