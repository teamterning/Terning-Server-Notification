package org.terning.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

@Embeddable
@EqualsAndHashCode
public class PushToken {

    private final String token;

    protected PushToken() {
        this.token = null;
    }
    private PushToken(String token) {
        validateToken(token);
        this.token = token;
    }

    public static PushToken from(String token) {
        return new PushToken(token);
    }

    // TODO: 추후 토큰 환경이 구축되면 토큰 데이터에 대한 비즈니스 로직 검증 추가
    // TODO: 해당 비즈니스 로직 테스트 코드 추가
    private void validateToken(String token) {
        validateNull(token);
    }

    private void validateNull(String token) {
        if (token == null) {
            throw new UserException(UserErrorCode.PUSH_TOKEN_NOT_NULL);
        }
    }

    public String value() {
        return token;
    }
}
