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
    }
}