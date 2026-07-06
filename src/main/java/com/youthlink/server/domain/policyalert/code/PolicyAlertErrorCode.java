package com.youthlink.server.domain.policyalert.code;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicyAlertErrorCode implements BaseErrorCode {
    ALERT_NOT_FOUND(HttpStatus.NOT_FOUND, "ALERT404", "구독 정보를 찾을 수 없습니다"),
    ALERT_FORBIDDEN(HttpStatus.FORBIDDEN, "ALERT403", "해당 구독에 대한 권한이 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
