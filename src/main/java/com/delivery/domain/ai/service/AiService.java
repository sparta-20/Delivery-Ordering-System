package com.delivery.domain.ai.service;

import com.delivery.domain.ai.dto.AiCreateRequest;
import com.delivery.domain.ai.dto.AiResponse;

public interface AiService {

    // AI 설명 생성 및 기록
    AiResponse createAiContent(Long userId, AiCreateRequest request);
}