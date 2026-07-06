package com.youthlink.server.domain.notification.code;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationSuccessCode implements BaseSuccessCode {
    NOTIFICATION_LIST_OK(HttpStatus.OK, "NOTI200", "알림 목록 조회 성공"),
    NOTIFICATION_READ_OK(HttpStatus.OK, "NOTI200", "알림 읽음 처리 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
