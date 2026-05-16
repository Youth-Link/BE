package com.youthlink.server.notification;

import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.repository.MemberRepository;
import com.youthlink.server.domain.notification.entity.Notification;
import com.youthlink.server.domain.notification.repository.NotificationRepository;
import com.youthlink.server.domain.notification.service.NotificationServiceImpl;
import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policyalert.entity.PolicyAlert;
import com.youthlink.server.domain.policyalert.repository.PolicyAlertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PolicyAlertRepository policyAlertRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("키워드와 지역이 모두 매칭되면 알림이 생성된다")
    void keywordAndRegionMatchCreatesNotification() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "일자리", "서울");
        Policy policy = createPolicy("청년 일자리 지원", "서울특별시");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));
        given(notificationRepository.existsByMemberAndPolicy(member, policy)).willReturn(false);

        notificationService.generateNotifications(List.of(policy));

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("키워드가 매칭되지 않으면 알림이 생성되지 않는다")
    void keywordMismatchDoesNotCreateNotification() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "주거", "서울");
        Policy policy = createPolicy("청년 일자리 지원", "서울특별시");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));

        notificationService.generateNotifications(List.of(policy));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("지역이 매칭되지 않으면 알림이 생성되지 않는다")
    void regionMismatchDoesNotCreateNotification() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "일자리", "부산");
        Policy policy = createPolicy("청년 일자리 지원", "서울특별시");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));

        notificationService.generateNotifications(List.of(policy));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("구독 지역이 null이면 전국 대상으로 매칭된다")
    void nullRegionMatchesAll() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "일자리", null);
        Policy policy = createPolicy("청년 일자리 지원", "제주특별자치도");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));
        given(notificationRepository.existsByMemberAndPolicy(member, policy)).willReturn(false);

        notificationService.generateNotifications(List.of(policy));

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("이미 알림이 생성된 정책은 중복 알림을 생성하지 않는다")
    void duplicateNotificationPrevented() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "일자리", null);
        Policy policy = createPolicy("청년 일자리 지원", "전국");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));
        given(notificationRepository.existsByMemberAndPolicy(member, policy)).willReturn(true);

        notificationService.generateNotifications(List.of(policy));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("알림 제목은 정책명을 포함한다")
    void notificationTitleContainsPolicyName() {
        Member member = createMember("user@test.com");
        PolicyAlert alert = createAlert(member, "일자리", null);
        Policy policy = createPolicy("청년 일자리 지원", "전국");

        given(policyAlertRepository.findAllByActiveTrue()).willReturn(List.of(alert));
        given(notificationRepository.existsByMemberAndPolicy(member, policy)).willReturn(false);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        notificationService.generateNotifications(List.of(policy));
        verify(notificationRepository).save(captor.capture());

        assertThat(captor.getValue().getTitle()).contains("청년 일자리 지원");
    }

    private Member createMember(String email) {
        return Member.builder().email(email).name("테스트유저").build();
    }

    private PolicyAlert createAlert(Member member, String keyword, String region) {
        return PolicyAlert.builder()
                .member(member)
                .keyword(keyword)
                .region(region)
                .build();
    }

    private Policy createPolicy(String name, String region) {
        return Policy.builder()
                .bizId("TEST001")
                .polyBizSjnm(name)
                .sporCn(name + " 지원 내용")
                .ctpvNm(region)
                .cnsgNmor("테스트 기관")
                .rqutPrdCn("2024.01.01 ~ 2024.12.31")
                .build();
    }
}
