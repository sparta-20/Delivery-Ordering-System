package com.delivery.domain.ai.service;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.user.entity.UserRoleEnum;

import java.util.UUID;

public interface AiService {

    // AI 설명 생성 및 기록
    AiRes createAiContent(Long userId, AiCreateReq request);

    // AI 기록 단건 조회
    AiRes getAi(Long userId, UserRoleEnum role, UUID aiId);

    // AI 기록 삭제
    void softDelete(UUID aiId, Long userId, UserRoleEnum role);
}