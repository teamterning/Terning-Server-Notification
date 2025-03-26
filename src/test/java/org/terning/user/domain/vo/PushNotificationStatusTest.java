package org.terning.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("푸시 알림 상태(PushNotificationStatus) 테스트")
class PushNotificationStatusTest {

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @ValueSource(strings = {"", "enable", "disable", "on", "off", "true", "false", "null", " "})
        @DisplayName("유효하지 않은 문자열로 of() 호출 시 예외 발생")
        void invalidStringThrowsException(String input) {
            assertThatThrownBy(() -> PushNotificationStatus.of(input))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_PUSH_STATUS.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "enable", "disable", "on", "off", "true", "false", "null", " "})
        @DisplayName("유효하지 않은 문자열은 isValid()가 false를 반환한다")
        void invalidStringReturnsFalse(String input) {
            assertThat(PushNotificationStatus.isValid(input)).isFalse();
        }

        @Test
        @DisplayName("ENABLED 상태에서 enable() 호출 시 예외 발생")
        void enableWhenAlreadyEnabledThrowsException() {
            assertThatThrownBy(() -> PushNotificationStatus.ENABLED.enable())
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.ALREADY_ENABLED_PUSH_NOTIFICATION.getMessage());
        }

        @Test
        @DisplayName("DISABLED 상태에서 disable() 호출 시 예외 발생")
        void disableWhenAlreadyDisabledThrowsException() {
            assertThatThrownBy(() -> PushNotificationStatus.DISABLED.disable())
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.ALREADY_DISABLED_PUSH_NOTIFICATION.getMessage());
        }
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @ValueSource(strings = {"ENABLED", "enabled", "EnAbLeD"})
        @DisplayName("유효한 문자열로 PushNotificationStatus.ENABLED를 생성할 수 있다")
        void createEnabledFromValidStrings(String input) {
            assertThat(PushNotificationStatus.of(input)).isEqualTo(PushNotificationStatus.ENABLED);
        }

        @ParameterizedTest
        @ValueSource(strings = {"DISABLED", "disabled", "DisAbLed"})
        @DisplayName("유효한 문자열로 PushNotificationStatus.DISABLED를 생성할 수 있다")
        void createDisabledFromValidStrings(String input) {
            assertThat(PushNotificationStatus.of(input)).isEqualTo(PushNotificationStatus.DISABLED);
        }

        @Test
        @DisplayName("ENABLED 상태일 때 canReceiveNotification은 true를 반환한다")
        void canReceiveNotificationWhenEnabled() {
            assertThat(PushNotificationStatus.ENABLED.canReceiveNotification()).isTrue();
        }

        @Test
        @DisplayName("DISABLED 상태일 때 canReceiveNotification은 false를 반환한다")
        void canNotReceiveNotificationWhenDisabled() {
            assertThat(PushNotificationStatus.DISABLED.canReceiveNotification()).isFalse();
        }

        @Test
        @DisplayName("DISABLED 상태에서 enable() 호출 시 ENABLED로 전환된다")
        void changeToEnabledFromDisabled() {
            PushNotificationStatus result = PushNotificationStatus.DISABLED.enable();
            assertThat(result).isEqualTo(PushNotificationStatus.ENABLED);
        }

        @Test
        @DisplayName("ENABLED 상태에서 disable() 호출 시 DISABLED로 전환된다")
        void changeToDisabledFromEnabled() {
            PushNotificationStatus result = PushNotificationStatus.ENABLED.disable();
            assertThat(result).isEqualTo(PushNotificationStatus.DISABLED);
        }

        @ParameterizedTest
        @ValueSource(strings = {"ENABLED", "DISABLED", "enabled", "disabled"})
        @DisplayName("유효한 문자열은 isValid()가 true를 반환한다")
        void validStringReturnsTrue(String input) {
            assertThat(PushNotificationStatus.isValid(input)).isTrue();
        }
    }
}
