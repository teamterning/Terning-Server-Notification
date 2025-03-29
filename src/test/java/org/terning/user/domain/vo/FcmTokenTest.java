package org.terning.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FCM 토큰 테스트")
class FcmTokenTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("유효한 FCM 토큰으로 생성할 수 있다.")
        void createFcmTokenWithValidValue() {
            // Given
            String validToken = "abcdefg123456";

            // When
            FcmToken fcmToken = FcmToken.from(validToken);

            // Then
            assertThat(fcmToken.value()).isEqualTo(validToken);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("FCM 토큰이 null이면 예외가 발생한다.")
        void shouldThrowExceptionWhenFcmTokenIsNull() {
            // Given
            String nullToken = null;

            // When & Then
            assertThatThrownBy(() -> FcmToken.from(nullToken))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.FCM_TOKEN_NOT_NULL.getMessage());
        }
    }
}
