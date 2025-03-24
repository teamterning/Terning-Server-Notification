package org.terning.user.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("유저 이름 테스트")
class UserNameTest {

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
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름은 null일 수 없습니다.");
        }

        @Test
        @DisplayName("이름의 길이가 12자를 초과하는 경우 예외가 발생한다")
        void shouldThrowExceptionForNameLengthExceeded() {
            // Given
            String invalidName = "1234567890123";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름의 길이는 12자를 초과할 수 없습니다.");
        }

        @Test
        @DisplayName("이름이 한글로만 구성되어 있고 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForHangulNameExceeding12Chars() {
            // Given
            String invalidName = "가나다라마바사아자차카다음";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름의 길이는 12자를 초과할 수 없습니다.");
        }

        @Test
        @DisplayName("이름이 영어로만 구성되어 있고 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionForEnglishNameExceeding12Chars() {
            // Given
            String invalidName = "abcdefghijklm";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름의 길이는 12자를 초과할 수 없습니다.");
        }

        @Test
        @DisplayName("이름에 공백이 포함되어 있고 전체 길이가 12자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenNameExceeds12IncludingSpaces() {
            // Given
            String invalidName = "장순 world2 123";

            // When & Then
            assertThatThrownBy(() -> UserName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름의 길이는 12자를 초과할 수 없습니다.");
        }
    }
}