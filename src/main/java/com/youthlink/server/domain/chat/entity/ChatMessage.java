package com.youthlink.server.domain.chat.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 사용자, AI - 이후 UI 렌더링.
    public enum Role {
        USER, ASSISTANT
    }

    @Builder
    public ChatMessage(Long memberId, String sessionId, Role role, String content) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.role = role;
        this.content = content;
    }
}
