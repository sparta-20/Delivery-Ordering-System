package com.delivery.domain.ai.controller;

import com.delivery.domain.ai.dto.AiCreateRequest;
import com.delivery.domain.ai.dto.AiResponse;
import com.delivery.domain.ai.service.AiService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    /**
     * AI 설명 생성 API
     *
     * @param userDetails 인증된 사용자 정보 (Spring Security)
     * @param request AI 생성 요청 정보
     * @return 201 Created + AI 생성 결과
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','MANAGER','OWNER')")
    public ResponseEntity<ApiResponse<AiResponse>> createAiContent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AiCreateRequest request) {

        AiResponse response = aiService.createAiContent(userDetails.getUser().getUserId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}