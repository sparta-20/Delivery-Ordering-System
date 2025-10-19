package com.delivery.domain.ai.service;

import com.delivery.domain.ai.client.GeminiAiClient;
import com.delivery.domain.ai.config.GeminiProperties;
import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.repository.AiRepository;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final GeminiAiClient geminiAiClient;
    private final AiRepository aiRepository;
    private final UserService userService;
    private final MenuService menuService;

    /** 공통 **/
    // AI 설명 생성 및 기록
    @Override
    @Transactional
    public AiRes createAi(Long userId, AiCreateReq request) {
        User user = userService.getUserById(userId);
        Menu menu = menuService.getMenuById(request.getMenuId());
        // Gemini API 호출
        String aiText  = geminiAiClient.generateContent(request.getPrompt(), request.getRequestType());
        // AI 요청 기록 저장
        Ai savedAi = saveAi(
                user,
                menu,
                request.getRequestType(),
                request.getPrompt(),
                aiText
        );
        return AiRes.from(savedAi);
    }

    // AI 요청 기록 저장
    private Ai saveAi(User user, Menu menu, RequestTypeEnum requestType,
                      String prompt, String aiResponse) {
        Ai ai = Ai.builder()
                .user(user)
                .menu(menu)
                .requestType(requestType)
                .prompt(prompt)
                .response(aiResponse)
                .build();
        return aiRepository.save(ai);
    }

    @Override
    public Page<AiSearchRes> searchAiRequests(
            Long userId, RequestTypeEnum requestType, UUID menuId,
            int page, int size, Sort.Direction direction
    ) {
        // Pageable 생성
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        try {
            Page<Ai> aiPage = aiRepository.searchAiRequests(userId, requestType, menuId, pageable);
            return aiPage.map(AiSearchRes::from);
        } catch (DataAccessException dae) {
            throw new BusinessException(ErrorCode.AI_SEARCH_FAILED);
        }
    }

    /** Admin **/
    // AI 요청 기록 단건 조회
    @Override
    public AiRes getAiForAdmin(UUID aiId) {
        return AiRes.from(getAiOrThrow(aiId));
    }

    // AI 요청 기록 논리 삭제
    @Override
    @Transactional
    public void softDeleteForAdmin(Long userId, UUID aiId) {
        Ai ai = getAiOrThrow(aiId);
        ai.markDeleted(userId);
    }

    // Ai 단건 조회 (lazy)
    private Ai getAiOrThrow(UUID aiId) {
        return aiRepository.findByAiIdAndDeletedAtIsNull(aiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AI_NOT_FOUND));
    }

    /** Owner **/
    // AI 요청 기록 단건 조회
    @Override
    public AiRes getAiForOwner(Long userId, UUID aiId) {
        return AiRes.from(findOwnedAiWithRelations(userId, aiId));
    }

    // AI 요청 기록 논리 삭제
    @Override
    @Transactional
    public void softDeleteForOwner(Long userId, UUID aiId) {
        Ai ai = findOwnedAiOrThrow(aiId, userId);
        ai.markDeleted(userId);
    }

    // OWNER 본인 소유 Ai 단건 조회 (lazy)
    private Ai findOwnedAiOrThrow(UUID aiId, Long userId) {
        return aiRepository.findByAiIdAndUser_UserIdAndDeletedAtIsNull(aiId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AI_NOT_FOUND));
    }

    // OWNER 본인 소유 Ai 단건 조회 (fetch join)
    private Ai findOwnedAiWithRelations(Long ownerId, UUID aiId) {
        return aiRepository.findDetailByUserId(aiId, ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AI_NOT_FOUND));
    }
}