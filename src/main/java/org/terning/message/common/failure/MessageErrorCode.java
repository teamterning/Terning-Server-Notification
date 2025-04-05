package org.terning.message.common.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.failure.ErrorCode;

@Getter
@AllArgsConstructor
public enum MessageErrorCode implements ErrorCode {
    MAIN_MESSAGE_NOT_NULL(HttpStatus.BAD_REQUEST, "메인 메시지는 null일 수 없습니다."),
    INVALID_MAIN_MESSAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 메인 메시지입니다."),

    MISSING_FORMATTING_PARAMS(HttpStatus.BAD_REQUEST, "메시지 포맷팅에 필요한 파라미터가 없습니다."),
    MISSING_PLACEHOLDER_VALUE(HttpStatus.BAD_REQUEST, "메시지 포맷팅에 필요한 값이 없습니다."),

    INVALID_SCHEDULE(HttpStatus.BAD_REQUEST, "유효하지 않은 스케줄입니다."),
    INVALID_EMPTY_SCHEDULE_LIST(HttpStatus.BAD_REQUEST, "스케줄 리스트는 비어 있을 수 없습니다."),


    ;

    private static final String PREFIX = "[MESSAGE ERROR] ";

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
