package com.delivery.domain.ai.dto;

import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public static AiRes from(Ai ai, Long userId, UUID menuId) {
        return AiRes.builder()
                .aiId(ai.getAiId())
                .userId(userId)
                .menuId(menuId)
                .requestType(ai.getRequestType())
                .prompt(ai.getPrompt())
                .response(ai.getResponse())
                .createdAt(ai.getCreatedAt())
                .build();
    }
}