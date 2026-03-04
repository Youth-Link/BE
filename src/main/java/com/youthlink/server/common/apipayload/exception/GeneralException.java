package com.youthlink.server.common.apipayload.exception;

import lombok.AllArgsConstructor;
import com.youthlink.server.common.apipayload.code.BaseErrorCode;

import lombok.Getter;

// 프로젝트 Exception
@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;
}
