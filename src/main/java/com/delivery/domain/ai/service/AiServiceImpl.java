package com.delivery.domain.ai.service;

import com.delivery.domain.ai.client.GeminiAiClient;
import com.delivery.domain.ai.config.GeminiProperties;
import com.delivery.domain.ai.dto.AiCreateRequest;
import com.delivery.domain.ai.dto.AiResponse;
import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.repository.AiRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final AiRepository aiRepository;
    private final UserService userService;
    private final GeminiAiClient geminiAiClient;
    private final GeminiProperties geminiProperties;

    // AI 설명 생성 및 기록
    @Override
    @Transactional
    public AiResponse createAiContent(Long userId, AiCreateRequest request) {
        log.info("[AI] 생성 시작 - userId: {}, menuId: {}, type: {}",
                userId, request.getMenuId(), request.getRequestType());

        User user = userService.getUserById(userId);

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
                request.getMenuId(),
                request.getRequestType(),
                request.getPrompt(),
                aiText
        );

        log.info("[AI] 생성 완료 - aiId: {}", savedAi.getAiId());
        return AiResponse.from(savedAi);
    }

    // 프롬프트 가공 (요구사항: 50자 이하 안내 문구 첨부)
    private String enhancePrompt(String prompt) {
        return prompt + geminiProperties.getPromptSuffix();
    }

    // AI 요청 기록 저장
    private Ai saveAi(User user, Long menuId, RequestTypeEnum requestType,
                        String prompt, String aiResponse) {

        Ai ai = Ai.builder()
                .user(user)
                .menuId(menuId)
                .requestType(requestType)
                .prompt(prompt)             // 원문 저장
                .response(aiResponse)
                .build();

        return aiRepository.save(ai);
    }
}