package org.terning.user.domain.vo;

import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import java.util.Arrays;

public enum AccountStatus {
    LOGGED_IN,
    LOGGED_OUT,
    WITHDRAWN;

    public static AccountStatus of(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_ACCOUNT_STATUS));
    }

    public static boolean isValid(String name) {
        return Arrays.stream(values())
                .anyMatch(status -> status.name().equalsIgnoreCase(name));
    }

    public boolean isLoggedIn() {
        return this == LOGGED_IN;
    }

    public boolean isLoggedOut() {
        return this == LOGGED_OUT;
    }

    public boolean isWithdrawn() {
        return this == WITHDRAWN;
    }
}

