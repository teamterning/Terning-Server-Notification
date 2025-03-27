package org.terning.scrap.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.terning.scrap.common.failure.ScrapErrorCode;
import org.terning.scrap.common.failure.ScrapException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("스크랩 상태 테스트")
class ScrapStatusTest {

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("null 값으로 from() 호출 시 예외 발생")
        void nullInputThrowsException() {
            assertThatThrownBy(() -> ScrapStatus.from(null))
                    .isInstanceOf(ScrapException.class)
                    .hasMessageContaining(ScrapErrorCode.SCRAP_STATUS_CANNOT_BE_NULL.getMessage());
        }

        @Test
        @DisplayName("null 값은 isValid()가 false를 반환한다")
        void nullInputReturnsFalseForIsValid() {
            assertThat(ScrapStatus.isValid(null)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "active", "inactive", "scrap", "true", "false", "null", " "})
        @DisplayName("유효하지 않은 문자열로 from() 호출 시 예외 발생")
        void invalidStringThrowsException(String input) {
            assertThatThrownBy(() -> ScrapStatus.from(input))
                    .isInstanceOf(ScrapException.class)
                    .hasMessageContaining(ScrapErrorCode.INVALID_SCRAP_STATUS.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "active", "inactive", "scrap", "true", "false", "null", " "})
        @DisplayName("유효하지 않은 문자열은 isValid()가 false를 반환한다")
        void invalidStringReturnsFalse(String input) {
            assertThat(ScrapStatus.isValid(input)).isFalse();
        }

        @Test
        @DisplayName("SCRAPPED 상태에서 scrap() 호출 시 예외 발생")
        void scrapWhenAlreadyScrappedThrowsException() {
            assertThatThrownBy(() -> ScrapStatus.SCRAPPED.scrap())
                    .isInstanceOf(ScrapException.class)
                    .hasMessageContaining(ScrapErrorCode.ALREADY_SCRAPPED.getMessage());
        }

        @Test
        @DisplayName("UNSCRAPPED 상태에서 unscrap() 호출 시 예외 발생")
        void unscrapWhenAlreadyUnscrappedThrowsException() {
            assertThatThrownBy(() -> ScrapStatus.UNSCRAPPED.unscrap())
                    .isInstanceOf(ScrapException.class)
                    .hasMessageContaining(ScrapErrorCode.ALREADY_UNSCRAPPED.getMessage());
        }
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @ValueSource(strings = {"scrapped", "SCRAPPED", "ScrApPeD"})
        @DisplayName("유효한 문자열로 SCRAPPED를 생성할 수 있다")
        void createScrappedFromValidStrings(String input) {
            assertThat(ScrapStatus.from(input)).isEqualTo(ScrapStatus.SCRAPPED);
        }

        @ParameterizedTest
        @ValueSource(strings = {"unscrapped", "UNSCRAPPED", "UnScRaPpEd"})
        @DisplayName("유효한 문자열로 UNSCRAPPED를 생성할 수 있다")
        void createUnscrappedFromValidStrings(String input) {
            assertThat(ScrapStatus.from(input)).isEqualTo(ScrapStatus.UNSCRAPPED);
        }

        @Test
        @DisplayName("SCRAPPED 상태는 isScrapped()가 true를 반환한다")
        void isScrappedTrueForScrapped() {
            assertThat(ScrapStatus.SCRAPPED.isScrapped()).isTrue();
        }

        @Test
        @DisplayName("UNSCRAPPED 상태는 isScrapped()가 false를 반환한다")
        void isScrappedFalseForUnscrapped() {
            assertThat(ScrapStatus.UNSCRAPPED.isScrapped()).isFalse();
        }

        @Test
        @DisplayName("UNSCRAPPED 상태에서 scrap() 호출 시 SCRAPPED로 전환된다")
        void changeToScrappedFromUnscrapped() {
            ScrapStatus result = ScrapStatus.UNSCRAPPED.scrap();
            assertThat(result).isEqualTo(ScrapStatus.SCRAPPED);
        }

        @Test
        @DisplayName("SCRAPPED 상태에서 unscrap() 호출 시 UNSCRAPPED로 전환된다")
        void changeToUnscrappedFromScrapped() {
            ScrapStatus result = ScrapStatus.SCRAPPED.unscrap();
            assertThat(result).isEqualTo(ScrapStatus.UNSCRAPPED);
        }

        @ParameterizedTest
        @ValueSource(strings = {"scrapped", "unscrapped", "SCRAPPED", "UNSCRAPPED"})
        @DisplayName("유효한 문자열은 isValid()가 true를 반환한다")
        void validStringReturnsTrue(String input) {
            assertThat(ScrapStatus.isValid(input)).isTrue();
        }
    }
}
