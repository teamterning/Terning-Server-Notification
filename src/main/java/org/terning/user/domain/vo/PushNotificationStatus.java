package org.terning.user.domain.vo;

import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import java.util.Arrays;

public enum PushNotificationStatus {
    ENABLED, DISABLED;

    public static PushNotificationStatus of(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_PUSH_STATUS));
    }

    public static boolean isValid(String name) {
        return Arrays.stream(values())
                .anyMatch(status -> status.name().equalsIgnoreCase(name));
    }

    public boolean canReceiveNotification() {
        return this == ENABLED;
    }

    public PushNotificationStatus enable() {
        if (this == ENABLED) {
            throw new UserException(UserErrorCode.ALREADY_ENABLED_PUSH_NOTIFICATION);
        }
        return ENABLED;
    }

    public PushNotificationStatus disable() {
        if (this == DISABLED) {
            throw new UserException(UserErrorCode.ALREADY_DISABLED_PUSH_NOTIFICATION);
        }
        return DISABLED;
    }
}
