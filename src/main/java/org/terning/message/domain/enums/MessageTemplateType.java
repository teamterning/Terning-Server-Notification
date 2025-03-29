package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public enum MessageTemplateType {

    INTERESTED_ANNOUNCEMENT_REMINDER(
            MainMessage.INTERESTED_ANNOUNCEMENT,
            SubMessage.INTERESTED_ANNOUNCEMENT_DETAIL,
            MessageTargetType.SCRAPPED_USER
    ),

    RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION(
            MainMessage.RECENTLY_POSTED_INTERNSHIP,
            SubMessage.RECENTLY_POSTED_INTERNSHIP_DETAIL,
            MessageTargetType.ALL_USERS
    ),

    TRENDING_INTERNSHIP_ALERT(
            MainMessage.TRENDING_INTERNSHIP,
            SubMessage.TRENDING_INTERNSHIP_DETAIL,
            MessageTargetType.ALL_USERS
    );

    private final MainMessage mainMessage;
    private final SubMessage subMessage;
    private final MessageTargetType targetType;

    public String main(Map<String, String> params) {
        return mainMessage.format(params);
    }

    public String sub(Map<String, String> params) {
        return subMessage.format(params);
    }

    public MessageTargetType targetType() {
        return targetType;
    }
}
