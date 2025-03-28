package org.terning.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

@Embeddable
@EqualsAndHashCode
public class FcmToken {

    private final String value;

    protected FcmToken() {
        this.value = null;
    }

    private FcmToken(String value) {
        validateToken(value);
        this.value = value;
    }

    public static FcmToken from(String value) {
        return new FcmToken(value);
    }

    // TODO: 추후 토큰 환경이 구축되면 토큰 데이터에 대한 비즈니스 로직 검증 추가
    // TODO: 해당 비즈니스 로직 테스트 코드 추가
    private void validateToken(String value) {
        validateNotNull(value);
    }

    private void validateNotNull(String value) {
        if (value == null) {
            throw new UserException(UserErrorCode.FCM_TOKEN_NOT_NULL);
        }
    }

    public String value() {
        return value;
    }
}
