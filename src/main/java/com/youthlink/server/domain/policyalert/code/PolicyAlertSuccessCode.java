package com.youthlink.server.domain.policyalert.code;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicyAlertSuccessCode implements BaseSuccessCode {
    ALERT_CREATED(HttpStatus.CREATED, "ALERT201", "정책 알림 구독 생성 성공"),
    ALERT_LIST_OK(HttpStatus.OK, "ALERT200", "정책 알림 구독 목록 조회 성공"),
    ALERT_DELETED(HttpStatus.OK, "ALERT200", "정책 알림 구독 삭제 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
