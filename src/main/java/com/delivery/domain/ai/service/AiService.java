package com.delivery.domain.ai.service;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;

public interface AiService {

    // AI 설명 생성 및 기록
    AiRes createAiContent(Long userId, AiCreateReq request);
}