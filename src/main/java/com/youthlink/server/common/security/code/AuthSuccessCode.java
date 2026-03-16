package com.youthlink.server.common.security.code;

import org.springframework.http.HttpStatus;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    REISSUE_SUCCESS(HttpStatus.OK, "AUTH200", "토큰 재발급 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH201", "로그아웃 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
