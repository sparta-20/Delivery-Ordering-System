package com.delivery.domain.ai.service;

import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface AiService {
    /** 공통 **/
    AiRes createAi(Long userId, AiCreateReq request);
    Page<AiSearchRes> searchAiRequests(
            Long userId, RequestTypeEnum requestType, UUID menuId,
            int page, int size, Sort.Direction direction
    );

    /** Owner **/
    AiRes getAiForOwner(Long userId, UUID aiId);
    void softDeleteForOwner(Long userId, UUID aiId);

    /** Admin **/
    AiRes getAiForAdmin(UUID aiId);
    void softDeleteForAdmin(Long userId, UUID aiId);
}