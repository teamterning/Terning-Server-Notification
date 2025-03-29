package org.terning.scrap.common.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.failure.ErrorCode;

@Getter
@AllArgsConstructor
public enum ScrapErrorCode implements ErrorCode {

    INVALID_SCRAP_STATUS(HttpStatus.BAD_REQUEST, "스크랩 상태는 scrapped, unscrapped만 가능합니다."),
    ALREADY_SCRAPPED(HttpStatus.BAD_REQUEST, "스크랩된 상태입니다."),
    ALREADY_UNSCRAPPED(HttpStatus.BAD_REQUEST, "스크랩되지 않은 상태입니다."),
    SCRAP_STATUS_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "스크랩 상태는 null일 수 없습니다."),

    ;

    private static final String PREFIX = "[SCRAP ERROR] ";

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
