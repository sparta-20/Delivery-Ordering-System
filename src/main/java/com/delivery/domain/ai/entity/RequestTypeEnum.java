package com.delivery.domain.ai.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// AI 요청 타입별 시스템 지시사항 관리
@Getter
@RequiredArgsConstructor
public enum RequestTypeEnum {

    MENU_DESCRIPTION(
            "당신은 배달 음식 메뉴를 소개하는 전문가입니다. " +
                    "사용자 입력을 바탕으로 고객이 주문하고 싶게 만드는 한 문장 설명을 작성하세요. " +
                    "정중하면서도 친근한 톤으로, 음식의 핵심 특징과 맛을 생생하게 표현하세요. "
    );

    private final String systemInstruction;
}