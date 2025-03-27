package org.terning.user.common.failure;

import org.terning.global.failure.BaseException;

public class UserException extends BaseException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
