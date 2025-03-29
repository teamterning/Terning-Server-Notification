package org.terning.fcm.common.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.failure.ErrorCode;

@Getter
@AllArgsConstructor
public enum FcmErrorCode implements ErrorCode {
    FCM_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 메시지 전송에 실패했습니다."),
    ;

    private static final String PREFIX = "[FCM ERROR] ";

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
