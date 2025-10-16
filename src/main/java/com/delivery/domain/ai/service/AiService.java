package com.delivery.domain.ai.service;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface AiService {

    // AI 설명 생성 및 기록
    AiRes createAiContent(Long userId, AiCreateReq request);

    // AI 기록 단건 조회
    AiRes getAi(Long userId, UserRoleEnum role, UUID aiId);

    // AI 기록 삭제
    void softDelete(UUID aiId, Long userId, UserRoleEnum role);

    // AI 기록 검색
    Page<AiSearchRes> searchAiRequests(RequestTypeEnum requestType, Long userId, UUID menuId, int page, int size, Sort.Direction direction, User user);
}