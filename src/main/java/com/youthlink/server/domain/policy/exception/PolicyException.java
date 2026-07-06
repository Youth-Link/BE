package com.youthlink.server.domain.policy.exception;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;

public class PolicyException extends GeneralException {
    public PolicyException(BaseErrorCode code) {
        super(code);
    }
}
