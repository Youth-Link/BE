package com.youthlink.server.domain.chat.dto;

import com.youthlink.server.domain.chat.entity.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(
        Long id,
        String sessionId,
        ChatMessage.Role role,
        String content,
        LocalDateTime createdAt) {
}
