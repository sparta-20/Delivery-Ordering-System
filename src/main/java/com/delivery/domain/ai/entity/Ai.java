package com.delivery.domain.ai.entity;

import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_ai")
@Getter
public class Ai extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID aiId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "menu_id", nullable = false)
    private UUID menuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestTypeEnum requestTypeEnum = RequestTypeEnum.MENU_DESCRIPTION;

    @Column(length = 500, nullable = false)
    private String prompt;

    private String response;

    @Builder
    public Ai(Long userId, UUID menuId, String prompt, String response) {
        this.userId = userId;
        this.menuId = menuId;
        this.prompt = prompt;
        this.response = response;
    }
}
