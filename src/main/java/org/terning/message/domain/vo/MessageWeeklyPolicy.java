package org.terning.message.domain.vo;

import org.terning.message.common.failure.MessageErrorCode;
import org.terning.message.common.failure.MessageException;

import java.time.LocalDateTime;
import java.util.List;

public record MessageWeeklyPolicy(List<Schedule> schedules) {

    public static MessageWeeklyPolicy of(List<Schedule> schedules) {
        return new MessageWeeklyPolicy(schedules);
    }

    public boolean isTodaySchedule(LocalDateTime now) {
        return schedules.stream().anyMatch(schedule -> schedule.matches(now));
    }

    public LocalDateTime nextScheduleDate(LocalDateTime now) {
        return schedules.stream()
                .map(schedule -> schedule.nextScheduleAfter(now))
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new MessageException(MessageErrorCode.INVALID_SCHEDULE));
    }
}
