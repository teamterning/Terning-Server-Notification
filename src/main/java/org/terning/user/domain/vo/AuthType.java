package org.terning.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import java.util.Arrays;

public enum AuthType {
    APPLE("apple"),
    KAKAO("kakao");

    private final String value;

    AuthType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AuthType from(String input) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_AUTH_TYPE));
    }

    public static boolean isValid(String input) {
        return Arrays.stream(values())
                .anyMatch(type -> type.value.equalsIgnoreCase(input));
    }

    @JsonValue
    public String value() {
        return value;
    }
}
