package com.delivery.domain.ai.dto;

import com.delivery.domain.ai.entity.RequestTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiCreateRequest {

    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

    @NotBlank(message = "프롬프트는 필수입니다.")
    @Size(max = 500, message = "프롬프트는 최대 500자까지 입력 가능합니다.")
    private String prompt;

    @NotNull(message = "요청 타입은 필수입니다.")
    private RequestTypeEnum requestType;
}