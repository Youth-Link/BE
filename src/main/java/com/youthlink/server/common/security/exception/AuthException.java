package com.youthlink.server.common.security.exception;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
