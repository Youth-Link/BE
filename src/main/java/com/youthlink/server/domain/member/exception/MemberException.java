package com.youthlink.server.domain.member.exception;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode code) {
        super(code);
    }
}
