package org.terning.message.common.failure;

import org.terning.global.failure.BaseException;

public class MessageException extends BaseException {
    public MessageException(MessageErrorCode errorCode) {
        super(errorCode);
    }
}
