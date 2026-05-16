package com.youthlink.server.domain.notification.service;

import com.youthlink.server.domain.notification.dto.NotificationResDto;
import com.youthlink.server.domain.policy.entity.Policy;

import java.util.List;

public interface NotificationService {
    /**
     * 신규/변경된 정책 목록에 대해 활성 구독 중인 사용자와 매칭하여 알림을 생성한다.
     */
    void generateNotifications(List<Policy> changedPolicies);

    List<NotificationResDto.NotificationDto> getMyNotifications(String email);

    void markAsRead(String email, Long notificationId);
}
