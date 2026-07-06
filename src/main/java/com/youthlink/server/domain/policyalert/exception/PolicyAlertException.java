package com.youthlink.server.domain.policyalert.exception;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;

public class PolicyAlertException extends GeneralException {
    public PolicyAlertException(BaseErrorCode code) {
        super(code);
    }
}
