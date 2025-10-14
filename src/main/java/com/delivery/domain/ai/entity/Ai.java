package com.delivery.domain.ai.entity;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.user.entity.User;
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
    @Column(name = "ai_id", nullable = false)
    private UUID aiId;

    // 사용자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 메뉴 (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestTypeEnum requestType;

    @Column(name = "prompt", nullable = false, length = 500)
    private String prompt;

    @Column(name = "response")
    private String response;

    @Builder
    public Ai(User user, Menu menu, RequestTypeEnum requestType, String prompt, String response) {
        this.user = user;
        this.menu = menu;
        this.requestType = requestType;
        this.prompt = prompt;
        this.response = response;
    }
}