package org.terning.user.common.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.failure.ErrorCode;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NAME_NOT_NULL(HttpStatus.BAD_REQUEST, "이름은 null일 수 없습니다."),
    USER_NAME_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "이름은 공백 포함 12글자를 넘을 수 없습니다."),
    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, "이름은 한글, 영어, 숫자, 공백으로만 구성할 수 있습니다."),

    FCM_TOKEN_NOT_NULL(HttpStatus.BAD_REQUEST, "FCM 토큰은 null일 수 없습니다."),

    INVALID_PUSH_STATUS(HttpStatus.BAD_REQUEST, "푸시 상태는 ON, OFF만 가능합니다."),

    ALREADY_ENABLED_PUSH_NOTIFICATION(HttpStatus.BAD_REQUEST, "이미 푸시 알림이 활성화되어 있습니다."),
    ALREADY_DISABLED_PUSH_NOTIFICATION(HttpStatus.BAD_REQUEST, "이미 푸시 알림이 비활성화되어 있습니다."),

    INVALID_ACCOUNT_STATUS(HttpStatus.BAD_REQUEST, "계정 상태는 active, inactive, withdrawn만 가능합니다."),

    INVALID_AUTH_TYPE(HttpStatus.BAD_REQUEST, "인증 타입은 apple, kakao만 가능합니다."),
    ;

    private static final String PREFIX = "[USER ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
