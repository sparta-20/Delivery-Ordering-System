package com.delivery.domain.ai.client;

import com.delivery.domain.ai.config.GeminiConfig;
import com.delivery.domain.ai.config.GeminiProperties;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiAiClient {

    private final Client geminiClient;
    private final GeminiConfig geminiConfig;
    private final GeminiProperties geminiProperties;

    // 텍스트 생성
    public String generateContent(String prompt, RequestTypeEnum requestType) {
        // Gemini 설정 조회
        GenerateContentConfig config = geminiConfig.getConfig(requestType);
        // Gemini 외부 API 호출
        final GenerateContentResponse response;
        try {
             response = geminiClient.models.generateContent(
                    geminiProperties.getModel(),
                     enhancePrompt(prompt),
                    config
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_API_ERROR);
        }
        // Gemini 응답 검증 및 반환
        return extractAndValidateResponse(response);
    }

    // 프롬프트 가공 (요구사항: 50자 이하 안내 문구 첨부)
    private String enhancePrompt(String prompt) {
        return prompt + geminiProperties.getPromptSuffix();
    }

    // Gemini 응답 텍스트 추출 및 검증
    private String extractAndValidateResponse(GenerateContentResponse response) {
        if (response == null || response.text() == null || response.text().isBlank()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_EMPTY);
        }
        return response.text().trim();
    }
}