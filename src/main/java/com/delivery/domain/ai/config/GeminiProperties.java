package com.delivery.domain.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.gemini")
public class GeminiProperties {

    private String apiKey;
    private String model;
    private Integer maxOutputTokens;
    private Float temperature;
    private String promptSuffix;
}