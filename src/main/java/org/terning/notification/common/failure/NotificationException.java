package org.terning.notification.common.failure;

import org.terning.global.failure.BaseException;
import org.terning.notification.domain.Notification;

public class NotificationException extends BaseException {
    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode);
    }
}
