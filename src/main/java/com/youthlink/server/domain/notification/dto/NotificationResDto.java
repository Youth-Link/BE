package com.youthlink.server.domain.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class NotificationResDto {

    @Builder
    public record NotificationDto(
            Long id,
            String title,
            String content,
            boolean read,
            Long policyId,
            String policyName,
            LocalDateTime createdAt
    ) {}
}
