package com.delivery.domain.ai.dto;

import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "AI 검색 결과 DTO")
@Getter
@Builder
public class AiSearchRes {
    private UUID aiId;
    private Long userId;
    private String nickname;
    private UUID menuId;
    private String menuName;
    private RequestTypeEnum requestType;
    private String prompt;
    private String response;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AiSearchRes from(Ai ai) {
        return AiSearchRes.builder()
                .aiId(ai.getAiId())
                .userId(ai.getUser().getUserId())
                .nickname(ai.getUser().getNickname())
                .menuId(ai.getMenu().getMenuId())
                .menuName(ai.getMenu().getName())
                .requestType(ai.getRequestType())
                .prompt(ai.getPrompt())
                .response(ai.getResponse())
                .createdAt(ai.getCreatedAt())
                .updatedAt(ai.getModifiedAt())
                .build();
    }
}