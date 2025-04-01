package org.terning.notification.common.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.failure.ErrorCode;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    INVALID_TARGET_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 타겟 타입입니다."),

    INVALID_TEMPLATE_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 템플릿 타입입니다."),
    ;

    private static final String PREFIX = "[NOTIFICATION ERROR] ";

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
