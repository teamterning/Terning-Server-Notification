package org.terning.fcm.common.failure;

import org.terning.global.failure.BaseException;

public class FcmException extends BaseException {
    public FcmException(FcmErrorCode errorCode) {
        super(errorCode);
    }
}
