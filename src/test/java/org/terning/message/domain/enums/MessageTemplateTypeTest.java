package org.terning.message.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.message.common.failure.MessageErrorCode;
import org.terning.message.common.failure.MessageException;
import org.terning.message.domain.AbstractMessageTemplate;
import org.terning.message.domain.MessageTemplate;
import org.terning.user.domain.vo.UserName;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메시지 템플릿 타입 테스트")
class MessageTemplateTypeTest {

    private static final UserName VALID_USER_NAME = UserName.from("장순");

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("null 파라미터로 포맷 시 예외 발생")
        void throwWhenNullParams() {
            MessageTemplateType type = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;

            assertThatThrownBy(() -> type.sub(null))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_FORMATTING_PARAMS.getMessage());
        }

        @Test
        @DisplayName("빈 파라미터로 포맷 시 예외 발생")
        void throwWhenEmptyParams() {
            MessageTemplateType type = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;

            assertThatThrownBy(() -> type.sub(Map.of()))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_FORMATTING_PARAMS.getMessage());
        }

        @Test
        @DisplayName("치환 키가 잘못된 경우 예외 발생")
        void throwWhenWrongPlaceholderKey() {
            MessageTemplateType type = MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION;

            assertThatThrownBy(() -> type.sub(Map.of("user", VALID_USER_NAME.value())))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_PLACEHOLDER_VALUE.getMessage());
        }

        @Test
        @DisplayName("치환 값이 null이면 예외 발생")
        void throwWhenPlaceholderValueIsNull() {
            MessageTemplateType type = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;

            Map<String, String> paramWithNull = new HashMap<>();
            paramWithNull.put("username", null);

            assertThatThrownBy(() -> type.sub(paramWithNull))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_PLACEHOLDER_VALUE.getMessage());
        }
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("INTERESTED_ANNOUNCEMENT_REMINDER 템플릿 포맷 정상")
        void formatInterestedAnnouncementReminder() {
            MessageTemplateType type = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;

            String main = type.main(Map.of("username", VALID_USER_NAME.value()));
            String sub = type.sub(Map.of("username", VALID_USER_NAME.value()));

            assertThat(main).isEqualTo(type.main(Map.of()));
            assertThat(sub).doesNotContain("{username}");
        }

        @Test
        @DisplayName("RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION 템플릿 포맷 정상")
        void formatRecentlyPostedInternshipRecommendation() {
            MessageTemplateType type = MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION;

            String main = type.main(Map.of("username", VALID_USER_NAME.value()));
            String sub = type.sub(Map.of("username", VALID_USER_NAME.value()));

            assertThat(main).isEqualTo(type.main(Map.of()));
            assertThat(sub).doesNotContain("{username}");
        }

        @Test
        @DisplayName("TRENDING_INTERNSHIP_ALERT 템플릿 포맷 정상")
        void formatTrendingInternshipAlert() {
            MessageTemplateType type = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;

            String main = type.main(Map.of("username", VALID_USER_NAME.value()));
            String sub = type.sub(Map.of("username", VALID_USER_NAME.value()));

            assertThat(main).isEqualTo(type.main(Map.of()));
            assertThat(sub).doesNotContain("{username}");
        }

        @Test
        @DisplayName("메인 메시지는 포맷 파라미터 없이도 동작해야 한다")
        void mainMessageDoesNotRequireParams() {
            for (MessageTemplateType type : MessageTemplateType.values()) {
                String main = type.main(null);
                String expected = type.main(Map.of());
                assertThat(main).isEqualTo(expected);
            }
        }

        @Test
        @DisplayName("여분의 파라미터가 있어도 무시하고 정상 포맷된다")
        void ignoreUnusedParams() {
            MessageTemplateType type = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;
            Map<String, String> params = Map.of("username", VALID_USER_NAME.value(), "extra", "무시됨");

            String sub = type.sub(params);

            assertThat(sub).doesNotContain("{username}");
        }

        @Test
        @DisplayName("중복된 플레이스홀더도 모두 정상 치환된다")
        void duplicatedPlaceholderIsReplaced() {
            MessageTemplate duplicatedTemplate = new AbstractMessageTemplate(
                    "{username}님 안녕하세요. 다시 한 번 {username}님 반가워요!",
                    true
            ) {};

            String result = duplicatedTemplate.format(Map.of("username", VALID_USER_NAME.value()));

            assertThat(result).isEqualTo("장순님 안녕하세요. 다시 한 번 장순님 반가워요!");
        }
    }
}
