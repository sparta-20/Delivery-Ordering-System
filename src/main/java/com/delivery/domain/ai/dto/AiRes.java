package com.delivery.domain.ai.dto;

import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "AI 응답 DTO")
@Getter
@Builder
public class AiRes {
    private UUID aiId;
    private Long userId;
    private UUID menuId;
    private RequestTypeEnum requestType;
    private String prompt;
    private String response;
    private LocalDateTime createdAt;

    // Entity → DTO 변환
    public static AiRes from(Ai ai) {
        return AiRes.builder()
                .aiId(ai.getAiId())
                .userId(ai.getUser().getUserId())
                .menuId(ai.getMenu().getMenuId())
                .requestType(ai.getRequestType())
                .prompt(ai.getPrompt())
                .response(ai.getResponse())
                .createdAt(ai.getCreatedAt())
                .build();
    }
}