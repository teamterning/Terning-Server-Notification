package org.terning.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("푸시 토큰 테스트")
class PushTokenTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("유효한 푸시 토큰으로 생성할 수 있다.")
        void createPushTokenWithValidValue() {
            // Given
            String validToken = "abcdefg123456";

            // When
            PushToken pushToken = PushToken.from(validToken);

            // Then
            assertThat(pushToken.value()).isEqualTo(validToken);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("토큰이 null이면 예외가 발생한다.")
        void shouldThrowExceptionWhenTokenIsNull() {
            // Given
            String nullToken = null;

            // When & Then
            assertThatThrownBy(() -> PushToken.from(nullToken))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.PUSH_TOKEN_NOT_NULL.getMessage());
        }
    }
}
