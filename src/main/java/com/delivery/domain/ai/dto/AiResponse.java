package com.delivery.domain.ai.dto;

import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AiResponse {
    private UUID aiId;
    private Long userId;
    private Long menuId;
    private RequestTypeEnum requestType;
    private String prompt;
    private String response;
    private LocalDateTime createdAt;

    // Entity → DTO 변환
    public static AiResponse from(Ai ai) {
        return AiResponse.builder()
                .aiId(ai.getAiId())
                .userId(ai.getUser().getUserId())
                .menuId(ai.getMenuId())
                .requestType(ai.getRequestType())
                .prompt(ai.getPrompt())
                .response(ai.getResponse())
                .createdAt(ai.getCreatedAt())
                .build();
    }
}