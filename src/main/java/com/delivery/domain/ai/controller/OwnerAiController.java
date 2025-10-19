package com.delivery.domain.ai.controller;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.service.AiService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Owner / AI", description = "가게 사장용 AI 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/ai")
@PreAuthorize("hasAnyRole('OWNER')")
public class OwnerAiController {

    private final AiService aiService;

    @Operation(
            summary = "AI 설명 생성 (가게 주인용)",
            description = """
            Gemini AI를 사용하여 메뉴 설명을 자동 생성합니다.
            - OWNER는 본인 가게의 메뉴만 생성할 수 있습니다.
            """
    )
    @PostMapping
    public ResponseEntity<ApiRes<AiRes>> createAi(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AiCreateReq request) {
        AiRes response = aiService.createAi(userDetails.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiRes.success(response));
    }

    @Operation(
            summary = "AI 요청 기록 단건 조회 (가게 주인용)",
            description = """
            AI 요청 기록을 ID로 조회합니다.
            - OWNER는 본인이 생성한 기록만 조회할 수 있습니다.
            """
    )
    @GetMapping("/{aiId}")
    public ResponseEntity<ApiRes<AiRes>> getAi(
            @PathVariable UUID aiId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AiRes response = aiService.getAiForOwner(userDetails.getUserId(), aiId);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "AI 요청 기록 삭제 (가게 주인용)",
            description = """
            AI 요청 기록을 논리 삭제(Soft Delete)합니다.
            - OWNER는 본인이 생성한 기록만 삭제할 수 있습니다.
            """
    )
    @DeleteMapping("/{aiId}")
    public ResponseEntity<Void> deleteAi(
            @PathVariable UUID aiId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        aiService.softDeleteForOwner(userDetails.getUserId(), aiId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "AI 요청 기록 검색 (가게 주인용)",
            description = """
            AI 요청 기록을 다양한 조건으로 검색합니다.
            - 조건: requestType, menuId
            - 기본 정렬: 생성일시 내림차순
            - 페이지 크기: 10, 30, 50만 허용 (기타 값은 10으로 강제)
            - OWNER는 본인의 기록만 조회할 수 있습니다.
            """
    )
    @GetMapping("/search")
    public ResponseEntity<ApiRes<Page<AiSearchRes>>> searchAiRequests(
            @RequestParam(required = false) RequestTypeEnum requestType,
            @RequestParam(required = false) UUID menuId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<AiSearchRes> result = aiService.searchAiRequests(
                userDetails.getUser().getUserId(),
                requestType, menuId,
                page, size, direction
        );
        return  ResponseEntity.ok(ApiRes.success(result));
    }
}
