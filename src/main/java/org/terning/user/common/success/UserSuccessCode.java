package org.terning.user.common.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.terning.global.success.SuccessCode;

@Getter
@AllArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    FCM_TOKEN_REISSUE_STATUS_PROVIDED(HttpStatus.OK, "FCM 토큰 재발급 여부를 성공적으로 확인했습니다."),
    USER_CREATED(HttpStatus.CREATED, "유저가 성공적으로 생성되었습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}

