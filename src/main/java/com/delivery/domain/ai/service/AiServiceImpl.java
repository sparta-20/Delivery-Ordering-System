package com.delivery.domain.ai.service;

import com.delivery.domain.ai.client.GeminiAiClient;
import com.delivery.domain.ai.config.GeminiProperties;
import com.delivery.domain.ai.dto.AiCreateRequest;
import com.delivery.domain.ai.dto.AiResponse;
import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.repository.AiRepository;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final GeminiProperties geminiProperties;
    private final GeminiAiClient geminiAiClient;
    private final AiRepository aiRepository;
    private final UserService userService;
    private final MenuService menuService;

    // AI 설명 생성 및 기록
    @Override
    @Transactional
    public AiResponse createAiContent(Long userId, AiCreateRequest request) {
        log.info("[AI] 생성 시작 - userId: {}, menuId: {}, type: {}",
                userId, request.getMenuId(), request.getRequestType());

        // 사용자 / 메뉴 조회
        User user = userService.getUserById(userId);
        Menu menu = menuService.getMenuById(request.getMenuId());

        // 권한 검증 (OWNER 본인 or MANAGER/MASTER)
        checkWritePermission(user, menu);

        // 프롬프트 가공
        String enhancedPrompt = enhancePrompt(request.getPrompt());

        // Gemini API 호출
        String aiText  = geminiAiClient.generateContent(
                enhancedPrompt,
                request.getRequestType()
        );

        // AI 요청 기록 저장
        Ai savedAi = saveAi(
                user,
                menu,
                request.getRequestType(),
                request.getPrompt(),
                aiText
        );

        log.info("[AI] 생성 완료 - aiId: {}", savedAi.getAiId());
        return AiResponse.from(savedAi, userId, menu.getMenuId());
    }

    // 프롬프트 가공 (요구사항: 50자 이하 안내 문구 첨부)
    private String enhancePrompt(String prompt) {
        return prompt + geminiProperties.getPromptSuffix();
    }

    // AI 요청 기록 저장
    private Ai saveAi(User user, Menu menu, RequestTypeEnum requestType,
                        String prompt, String aiResponse) {

        Ai ai = Ai.builder()
                .user(user)
                .menu(menu)
                .requestType(requestType)
                .prompt(prompt)             // 원문 저장
                .response(aiResponse)
                .build();

        return aiRepository.save(ai);
    }

    // 권한 검증: OWNER는 자신의 가게 메뉴만, MANAGER/MASTER는 전체 허용.
    private void checkWritePermission(User user, Menu menu) {
        // MASTER, MANAGER → 전체 접근 허용
        switch (user.getRole()) {
            case MASTER, MANAGER -> { return; }

            case OWNER -> {
                if (!menu.getStore().getOwner().getUserId().equals(user.getUserId())) {
                    log.warn("[AI] 접근 거부 - userId: {}, menuId: {}, role: {}",
                            user.getUserId(), menu.getMenuId(), user.getRole());
                    throw new BusinessException(ErrorCode.MENU_ACCESS_DENIED);
                }
            }

            default -> {
                log.warn("[AI] 접근 거부 - userId: {}, role: {}",
                        user.getUserId(), user.getRole());
                throw new BusinessException(ErrorCode.MENU_ACCESS_DENIED);
            }
        }
    }
}