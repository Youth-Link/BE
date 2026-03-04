package com.youthlink.server.domain.member.code;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER4002", "이미 존재하는 사용자입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
