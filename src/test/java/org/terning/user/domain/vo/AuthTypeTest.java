package org.terning.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("인증 타입 테스트")
class AuthTypeTest {

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"google", "naver", " ", "앱"})
        @DisplayName("AuthType.from(String) 호출 시 유효하지 않은 값이면 예외가 발생한다")
        void throwsException_whenInvalidInputProvided(String input) {
            assertThatThrownBy(() -> AuthType.from(input))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_AUTH_TYPE.getMessage());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"google", "naver", " ", "앱"})
        @DisplayName("AuthType.isValid(String)는 유효하지 않은 문자열에 대해 false를 반환한다")
        void returnsFalse_whenInvalidInputProvided(String input) {
            assertThat(AuthType.isValid(input)).isFalse();
        }
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @CsvSource({
                "apple, APPLE",
                "kakao, KAKAO"
        })
        @DisplayName("AuthType.from(String)는 대소문자 구분 없이 정확한 Enum을 반환한다")
        void returnsCorrectEnum_whenValidInputProvided(String input, AuthType expected) {
            assertThat(AuthType.from(input)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"apple", "APPLE", "kakao", "KAKAO"})
        @DisplayName("AuthType.isValid(String)는 유효한 문자열에 대해 true를 반환한다")
        void returnsTrue_whenValidInputProvided(String input) {
            assertThat(AuthType.isValid(input)).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
                "APPLE, apple",
                "KAKAO, kakao"
        })
        @DisplayName("value() 메서드는 Enum에 대응되는 문자열을 반환한다 (직렬화 값 확인)")
        void valueMethodReturnsCorrectString(AuthType authType, String expectedValue) {
            assertThat(authType.value()).isEqualTo(expectedValue);
        }
    }
}
