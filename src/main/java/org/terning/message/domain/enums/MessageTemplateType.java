package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;
import org.terning.notification.common.failure.NotificationErrorCode;
import org.terning.notification.common.failure.NotificationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum MessageTemplateType {

    INTERESTED_ANNOUNCEMENT_REMINDER(
            MainMessage.INTERESTED_ANNOUNCEMENT,
            SubMessage.INTERESTED_ANNOUNCEMENT_DETAIL,
            MessageTargetType.SCRAPPED_USER,
            SendSchedulePolicy.INTERESTED_ANNOUNCEMENT
    ),
    RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION(
            MainMessage.RECENTLY_POSTED_INTERNSHIP,
            SubMessage.RECENTLY_POSTED_INTERNSHIP_DETAIL,
            MessageTargetType.ALL_USERS,
            SendSchedulePolicy.INTERNSHIP_RECOMMENDATION
    ),
    TRENDING_INTERNSHIP_ALERT(
            MainMessage.TRENDING_INTERNSHIP,
            SubMessage.TRENDING_INTERNSHIP_DETAIL,
            MessageTargetType.ALL_USERS,
            SendSchedulePolicy.TRENDING_INTERNSHIP
    );

    private final MainMessage mainMessage;
    private final SubMessage subMessage;
    private final MessageTargetType messageTargetType;
    private final SendSchedulePolicy schedule;

    public static MessageTemplateType from(String template) {
        return Stream.of(values())
                .filter(type -> type.name().equals(template))
                .findFirst()
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.INVALID_TEMPLATE_TYPE));
    }

    public String getImageUrl() {
        return switch (this) {
            case INTERESTED_ANNOUNCEMENT_REMINDER ->
                    "https://github.com/teamterning/Terning-Banner/blob/main/imageUrl/image_scrap.png";
            case RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION ->
                    "https://github.com/teamterning/Terning-Banner/blob/main/imageUrl/image_home.png";
            case TRENDING_INTERNSHIP_ALERT ->
                    "https://github.com/teamterning/Terning-Banner/blob/main/imageUrl/image_search.png";
        };
    }

    public String main(Map<String, String> params) {
        return mainMessage.format(params);
    }

    public String sub(Map<String, String> params) {
        return subMessage.format(params);
    }

    public MessageTargetType targetType() {
        return messageTargetType;
    }

    public SendSchedulePolicy schedule() {
        return schedule;
    }

    public boolean matchesTarget(MessageTargetType target) {
        return messageTargetType == target;
    }

    public boolean isTodayScheduled(LocalDateTime now) {
        return schedule.isDue(now);
    }

    public LocalDateTime nextScheduledDate(LocalDateTime now) {
        return schedule.nextScheduleAfter(now);
    }
}

