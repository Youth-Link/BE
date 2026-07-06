package com.youthlink.server.domain.notification.exception;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import com.youthlink.server.common.apipayload.exception.GeneralException;

public class NotificationException extends GeneralException {
    public NotificationException(BaseErrorCode code) {
        super(code);
    }
}
