package org.terning.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("계정 상태 테스트")
class AccountStatusTest {

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"deleted", "invalid", " ", "로그인중"})
        @DisplayName("AccountStatus.from(String) 호출 시 유효하지 않은 값이면 예외가 발생한다")
        void throwsException_whenInvalidStringProvided(String input) {
            assertThatThrownBy(() -> AccountStatus.from(input))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_ACCOUNT_STATUS.getMessage());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"deleted", "invalid", " ", "로그인중"})
        @DisplayName("AccountStatus.isValid(String)는 유효하지 않은 문자열에 대해 false를 반환한다")
        void returnsFalse_whenInvalidInputProvided(String input) {
            assertThat(AccountStatus.isValid(input)).isFalse();
        }
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @CsvSource({
                "active, ACTIVE",
                "inactive, INACTIVE",
                "WITHDRAWN, WITHDRAWN"
        })
        @DisplayName("AccountStatus.from(String)는 대소문자 구분 없이 올바른 Enum을 반환한다")
        void returnsCorrectEnum_whenValidStringProvided(String input, AccountStatus expected) {
            assertThat(AccountStatus.from(input)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"active", "INACTIVE", "withdrawn"})
        @DisplayName("AccountStatus.isValid(String)는 유효한 문자열에 대해 true를 반환한다")
        void returnsTrue_whenValidStringProvided(String input) {
            assertThat(AccountStatus.isValid(input)).isTrue();
        }

        @Test
        @DisplayName("ACTIVE 상태일 때 isActive()는 true를 반환한다")
        void isActiveReturnsTrue() {
            assertThat(AccountStatus.ACTIVE.isActive()).isTrue();
        }

        @Test
        @DisplayName("INACTIVE 상태일 때 isInactive()는 true를 반환한다")
        void isInactiveReturnsTrue() {
            assertThat(AccountStatus.INACTIVE.isInactive()).isTrue();
        }

        @Test
        @DisplayName("WITHDRAWN 상태일 때 isWithdrawn()는 true를 반환한다")
        void isWithdrawnReturnsTrue() {
            assertThat(AccountStatus.WITHDRAWN.isWithdrawn()).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
                "ACTIVE, active",
                "INACTIVE, inactive",
                "WITHDRAWN, withdrawn"
        })
        @DisplayName("value() 메서드는 Enum에 대응되는 문자열을 반환한다 (직렬화 값 확인)")
        void valueMethodReturnsCorrectString(AccountStatus status, String expected) {
            assertThat(status.value()).isEqualTo(expected);
        }
    }
}
