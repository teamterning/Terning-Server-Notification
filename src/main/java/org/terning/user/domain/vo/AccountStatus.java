package org.terning.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import java.util.Arrays;

public enum AccountStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    WITHDRAWN("withdrawn");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AccountStatus from(String input) {
        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_ACCOUNT_STATUS));
    }

    public static boolean isValid(String input) {
        return Arrays.stream(values())
                .anyMatch(status -> status.value.equalsIgnoreCase(input));
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isWithdrawn() {
        return this == WITHDRAWN;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
