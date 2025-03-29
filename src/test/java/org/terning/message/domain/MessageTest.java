package org.terning.message.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.message.common.failure.MessageErrorCode;
import org.terning.message.common.failure.MessageException;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.user.domain.vo.UserName;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메세지 도메인 테스트")
class MessageTest {

    private static final UserName VALID_USER_NAME = UserName.from("장순");
    private static final Map<String, String> VALID_PARAMS = Map.of("username", VALID_USER_NAME.value());

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("INTERESTED_ANNOUNCEMENT_REMINDER 메시지 생성이 정상적으로 수행된다")
        void createInterestedAnnouncementReminderMessage() {
            MessageTemplateType template = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;
            Message message = Message.of(template, VALID_PARAMS);

            assertThat(message.getFormattedMainMessage()).isEqualTo(template.main(VALID_PARAMS));
            assertThat(message.getFormattedSubMessage()).doesNotContain("{username}");
        }

        @Test
        @DisplayName("RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION 메시지 생성이 정상적으로 수행된다")
        void createRecentlyPostedInternshipRecommendationMessage() {
            MessageTemplateType template = MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION;
            Message message = Message.of(template, VALID_PARAMS);

            assertThat(message.getFormattedMainMessage()).isEqualTo(template.main(VALID_PARAMS));
            assertThat(message.getFormattedSubMessage()).doesNotContain("{username}");
        }

        @Test
        @DisplayName("TRENDING_INTERNSHIP_ALERT 메시지 생성이 정상적으로 수행된다")
        void createTrendingInternshipAlertMessage() {
            MessageTemplateType template = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;
            Message message = Message.of(template, VALID_PARAMS);

            assertThat(message.getFormattedMainMessage()).isEqualTo(template.main(VALID_PARAMS));
            assertThat(message.getFormattedSubMessage()).doesNotContain("{username}");
        }

        @Test
        @DisplayName("동일한 템플릿 타입인지 확인할 수 있다")
        void checkIfSameTemplateType() {
            MessageTemplateType template = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;
            Message message = Message.of(template, VALID_PARAMS);

            assertThat(message.isSameType(MessageTemplateType.TRENDING_INTERNSHIP_ALERT)).isTrue();
            assertThat(message.isSameType(MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION)).isFalse();
        }

        @Test
        @DisplayName("포맷이 필요 없는 템플릿은 파라미터 없이도 생성할 수 있다")
        void createWithoutParamsIfNoFormatNeeded() {
            for (MessageTemplateType template : MessageTemplateType.values()) {
                Map<String, String> params = Map.of("username", VALID_USER_NAME.value());
                assertThat(template.main(null)).isEqualTo(template.main(Map.of()));
                Message message = Message.of(template, params);
                assertThat(message).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("필요한 파라미터가 없으면 예외가 발생한다")
        void throwWhenMissingParams() {
            MessageTemplateType template = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;

            assertThatThrownBy(() -> Message.of(template, Map.of()))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_FORMATTING_PARAMS.getMessage());
        }

        @Test
        @DisplayName("null 파라미터가 전달되면 예외가 발생한다")
        void throwWhenNullParams() {
            MessageTemplateType template = MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION;

            assertThatThrownBy(() -> Message.of(template, null))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_FORMATTING_PARAMS.getMessage());
        }

        @Test
        @DisplayName("치환 키가 틀린 경우 예외가 발생한다")
        void throwWhenWrongPlaceholderKey() {
            MessageTemplateType template = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;
            Map<String, String> wrongKey = Map.of("wrong_key", VALID_USER_NAME.value());

            assertThatThrownBy(() -> Message.of(template, wrongKey))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_PLACEHOLDER_VALUE.getMessage());
        }

        @Test
        @DisplayName("치환 값이 null이면 예외가 발생한다")
        void throwWhenNullPlaceholderValue() {
            MessageTemplateType template = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;
            Map<String, String> paramWithNull = new HashMap<>();
            paramWithNull.put("username", null);

            assertThatThrownBy(() -> Message.of(template, paramWithNull))
                    .isInstanceOf(MessageException.class)
                    .hasMessageContaining(MessageErrorCode.MISSING_PLACEHOLDER_VALUE.getMessage());
        }
    }
}
