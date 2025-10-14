package com.delivery.domain.ai.config;

import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(GeminiProperties.class)
@RequiredArgsConstructor
public class GeminiConfig {

    private final GeminiProperties geminiProperties;
    private ImmutableMap<RequestTypeEnum, GenerateContentConfig> configCache;

    // Gemini API Client
    @Bean
    public Client geminiClient() {
        return Client.builder()
                .apiKey(geminiProperties.getApiKey())
                .build();
    }

    // 앱 시작 시 RequestType별 Config 캐싱
    @PostConstruct
    public void initConfigs() {
        ImmutableMap.Builder<RequestTypeEnum, GenerateContentConfig> builder = ImmutableMap.builder();

        // 모든 RequestTypeEnum에 대해 Config 생성
        for (RequestTypeEnum type : RequestTypeEnum.values()) {
            GenerateContentConfig config = createConfig(type);
            builder.put(type, config);
        }

        // 불변 Map 생성
        configCache = builder.build();
        log.info("[Gemini] Config 캐싱 완료 (총 {}개)", configCache.size());
    }

    // RequestType별 Config 조회
    public GenerateContentConfig getConfig(RequestTypeEnum requestType) {
        GenerateContentConfig config = configCache.get(requestType);

        if (config == null) {
            log.error("[Gemini] 지원하지 않는 RequestType 요청: {}", requestType);
            throw new BusinessException(ErrorCode.AI_UNSUPPORTED_REQUEST_TYPE);
        }

        return config;
    }

    // Config 생성
    private GenerateContentConfig createConfig(RequestTypeEnum requestType) {
        return GenerateContentConfig.builder()
                .thinkingConfig(ThinkingConfig.builder().thinkingBudget(0)) // 생각 토큰 0으로 끔
                .candidateCount(1)
                .temperature(geminiProperties.getTemperature())
                .maxOutputTokens(geminiProperties.getMaxOutputTokens())
                .safetySettings(createSafetySettings())
                .systemInstruction(createSystemInstruction(requestType))
                .build();
    }

    // 안전 설정(혐오/성적 콘텐츠 차단)
    private ImmutableList<SafetySetting> createSafetySettings() {
        return ImmutableList.of(
                SafetySetting.builder()
                        .category(HarmCategory.Known.HARM_CATEGORY_HATE_SPEECH) // 혐오 발언 차단
                        .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                        .build(),
                SafetySetting.builder()
                        .category(HarmCategory.Known.HARM_CATEGORY_SEXUALLY_EXPLICIT)   // 성적 콘텐츠 차단
                        .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                        .build()
        );
    }

    // 시스템 지시사항 생성
    private Content createSystemInstruction(RequestTypeEnum requestType) {
        return Content.fromParts(
                Part.fromText(requestType.getSystemInstruction())
        );
    }
}