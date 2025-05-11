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

@DisplayName("유저 이름 테스트")
class UserNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("문자(영어, 한글)와 숫자로 구성된 이름으로 만들 수 있다.")
        void createUserNameWithCharacterAndNumbers() {
            // Given
            String validName = "장순world2";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }

        @Test
        @DisplayName("영어 문자만으로 구성된 이름으로 만들 수 있다.")
        void createUserNameWithAlphabetsOnly() {

            // Given
            String validName = "jsoonworld";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }

        @Test
        @DisplayName("한글 문자만으로 구성된 이름으로 만들 수 있다.")
        void createUserNameWithHangulOnly() {

            // Given
            String validName = "한글이름";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }

        @Test
        @DisplayName("숫자로만 구성된 이름으로 만들 수 있다.")
        void createUserNameWithNumbers() {

            // Given
            String validName = "1234567890";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }

        @Test
        @DisplayName("12글자 이하의 이름을 만들 수 있다.")
        void createUserNameWithLessThan12Characters() {

            // Given
            String validName = "123456789012";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }

        @Test
        @DisplayName("공백을 포함하여 12글자 이하의 이름으로 만들 수 있다.")
        void createUserNameWithLessThan12CharactersIncludingSpace() {

            // Given
            String validName = "123 567 901";

            // When
            UserName userName = UserName.from(validName);

            // Then
            assertThat(userName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("이름이 null인 경우 예외가 발생한다.")
        void shouldThrowExceptionForNullName() {
            // Given
            String invalidName = null;

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NAME_NOT_NULL.getMessage());
        }

        @Test
        @DisplayName("이름의 길이가 12자를 초과하는 경우 예외가 발생한다")
        void shouldThrowExceptionForNameLengthExceeded() {
            // Given
            String invalidName = "1234567890123";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @Test
        @DisplayName("이름이 한글로만 구성되어 있고 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForHangulNameExceeding12Chars() {
            // Given
            String invalidName = "가나다라마바사아자차카다음";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @Test
        @DisplayName("이름이 영어로만 구성되어 있고 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForEnglishNameExceeding12Chars() {
            // Given
            String invalidName = "abcdefghijklm";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @Test
        @DisplayName("이름에 공백이 포함되어 있고 전체 길이가 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenNameExceeds12IncludingSpaces() {
            // Given
            String invalidName = "장순 world2 123";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NAME_LENGTH_EXCEEDED.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 특수문자가 포함된 경우 예외가 발생한다")
        @ValueSource(strings = {"jang_soon", "터닝@name", "hello#world", "test!", "name%"})
        void shouldThrowExceptionForSpecialCharacters(String invalidName) {
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_USER_NAME.getMessage());
        }

        @ParameterizedTest
        @DisplayName("이름에 제어 문자(공백 이외)가 포함된 경우 예외가 발생한다")
        @ValueSource(strings = {"이름\t입력", "이름\n입력", "이름\r입력", "이름\b입력"})
        void shouldThrowExceptionForControlCharacters(String invalidName) {
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_USER_NAME.getMessage());
        }

        @Test
        @DisplayName("이름에 이모지가 포함된 경우 예외가 발생한다")
        void shouldThrowExceptionForNameWithEmoji() {
            // Given
            String invalidName = "장순😊";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.INVALID_USER_NAME.getMessage());
        }
    }
}