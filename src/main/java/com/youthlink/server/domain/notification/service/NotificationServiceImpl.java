package com.youthlink.server.domain.notification.service;

import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import com.youthlink.server.domain.notification.code.NotificationErrorCode;
import com.youthlink.server.domain.notification.converter.NotificationConverter;
import com.youthlink.server.domain.notification.dto.NotificationResDto;
import com.youthlink.server.domain.notification.entity.Notification;
import com.youthlink.server.domain.notification.exception.NotificationException;
import com.youthlink.server.domain.notification.repository.NotificationRepository;
import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policyalert.entity.PolicyAlert;
import com.youthlink.server.domain.policyalert.repository.PolicyAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PolicyAlertRepository policyAlertRepository;
    private final MemberRepository memberRepository;

    @Override
    public void generateNotifications(List<Policy> changedPolicies) {
        if (changedPolicies == null || changedPolicies.isEmpty()) return;

        List<PolicyAlert> activeAlerts = policyAlertRepository.findAllByActiveTrue();

        for (Policy policy : changedPolicies) {
            for (PolicyAlert alert : activeAlerts) {
                if (!matchesPolicy(alert, policy)) continue;
                if (notificationRepository.existsByMemberAndPolicy(alert.getMember(), policy)) continue;

                Notification notification = Notification.builder()
                        .member(alert.getMember())
                        .policy(policy)
                        .title("[새 정책] " + nullToEmpty(policy.getPolyBizSjnm()))
                        .content(buildContent(policy))
                        .build();
                notificationRepository.save(notification);
            }
        }

        log.info("알림 매칭 완료 - 대상 정책 {}건", changedPolicies.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResDto.NotificationDto> getMyNotifications(String email) {
        Member member = findMember(email);
        return notificationRepository.findByMemberOrderByCreatedAtDesc(member).stream()
                .map(NotificationConverter::toNotificationDto)
                .toList();
    }

    @Override
    public void markAsRead(String email, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getMember().getEmail().equals(email)) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_FORBIDDEN);
        }

        notification.markAsRead();
    }

    private boolean matchesPolicy(PolicyAlert alert, Policy policy) {
        String keyword = alert.getKeyword();
        boolean keywordMatch =
                contains(policy.getPolyBizSjnm(), keyword) ||
                contains(policy.getSporCn(), keyword) ||
                contains(policy.getPolyItcnCn(), keyword);

        String alertRegion = alert.getRegion();
        boolean regionMatch = alertRegion == null || alertRegion.isBlank() ||
                contains(policy.getCtpvNm(), alertRegion);

        return keywordMatch && regionMatch;
    }

    private boolean contains(String text, String keyword) {
        return text != null && keyword != null && text.contains(keyword);
    }

    private String buildContent(Policy policy) {
        return String.format("기관: %s | 지역: %s | 신청기간: %s",
                nullToEmpty(policy.getCnsgNmor()),
                nullToEmpty(policy.getCtpvNm()),
                nullToEmpty(policy.getRqutPrdCn()));
    }

    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
