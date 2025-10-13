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

        log.info("[Gemini] 요청 시작 - model: {}, type: {}, promptLen: {}",
                geminiProperties.getModel(), requestType, prompt.length());

        final GenerateContentResponse response;
        try {
             response = geminiClient.models.generateContent(
                    geminiProperties.getModel(),
                    prompt,
                    config
            );
        } catch (Exception e) {
            log.error("[Gemini] 호출 실패 - model: {}, type: {}", geminiProperties.getModel(), requestType, e);
            throw new BusinessException(ErrorCode.AI_API_ERROR);
        }

        String responseText = extractAndValidateResponse(response);

        log.info("[Gemini] 응답 성공 - responseLen: {}", responseText.length());
        return responseText;
    }

    // Gemini 응답 텍스트 추출 및 검증
    private String extractAndValidateResponse(GenerateContentResponse response) {
        if (response == null || response.text() == null || response.text().isBlank()) {
            log.error("[Gemini] 응답이 비어있음");
            throw new BusinessException(ErrorCode.AI_RESPONSE_EMPTY);
        }

        String text = response.text().trim();

        // 50자 초과 시 경고
        if (text.length() > 50) {
            log.warn("[Gemini] 응답 50자 초과 - len: {}", text.length());
        }

        return text;
    }
}