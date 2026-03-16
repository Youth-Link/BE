package com.youthlink.server.domain.member.code;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements BaseSuccessCode {
    MEMBER_OK(HttpStatus.OK, "MEMBER200", "회원 관련 요청 성공"),
    MEMBER_JOINED(HttpStatus.CREATED, "MEMBER201", "회원 가입 성공"),
    MEMBER_UPDATE_SUCCESS(HttpStatus.OK, "MEMBER201", "회원 정보 수정 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
