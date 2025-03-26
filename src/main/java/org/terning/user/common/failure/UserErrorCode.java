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
    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, "이름의 구성은 문자(한글, 영어), 숫자만 가능합니다."),

    PUSH_TOKEN_NOT_NULL(HttpStatus.BAD_REQUEST, "푸시 토큰은 null일 수 없습니다."),

    INVALID_PUSH_STATUS(HttpStatus.BAD_REQUEST, "푸시 상태는 ON, OFF만 가능합니다."),

    ALREADY_ENABLED_PUSH_NOTIFICATION(HttpStatus.BAD_REQUEST, "이미 푸시 알림이 활성화되어 있습니다."),
    ALREADY_DISABLED_PUSH_NOTIFICATION(HttpStatus.BAD_REQUEST, "이미 푸시 알림이 비활성화되어 있습니다."),

    INVALID_ACCOUNT_STATUS(HttpStatus.BAD_REQUEST, "계정 상태는 active, inactive, withdrawn만 가능합니다."),
    ;

    private static final String PREFIX = "[USER ERROR] ";
    public static final int NAME_MAX_LENGTH = 12;

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
