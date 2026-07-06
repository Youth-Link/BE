package com.youthlink.server.domain.notification.converter;

import com.youthlink.server.domain.notification.dto.NotificationResDto;
import com.youthlink.server.domain.notification.entity.Notification;

public class NotificationConverter {

    public static NotificationResDto.NotificationDto toNotificationDto(Notification notification) {
        return NotificationResDto.NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .read(notification.isRead())
                .policyId(notification.getPolicy() != null ? notification.getPolicy().getId() : null)
                .policyName(notification.getPolicy() != null ? notification.getPolicy().getPolyBizSjnm() : null)
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
