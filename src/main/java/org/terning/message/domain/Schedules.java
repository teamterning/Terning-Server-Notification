package org.terning.message.domain;

import org.terning.message.common.failure.MessageErrorCode;
import org.terning.message.common.failure.MessageException;
import org.terning.message.domain.vo.Schedule;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Schedules {

    private final List<Schedule> values;

    private Schedules(List<Schedule> values) {
        this.values = validate(values);
    }

    public static Schedules of(List<Schedule> values) {
        return new Schedules(values);
    }

    public LocalDateTime nextScheduleDate(LocalDateTime now) {
        return values.stream()
                .map(schedule -> schedule.nextScheduleAfter(now))
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new MessageException(MessageErrorCode.INVALID_SCHEDULE));
    }

    public boolean isTodaySchedule(LocalDateTime now) {
        return values.stream().anyMatch(schedule -> schedule.matches(now));
    }

    public List<Schedule> getValues() {
        return Collections.unmodifiableList(values);
    }

    private static List<Schedule> validate(List<Schedule> values) {
        if (values == null || values.isEmpty()) {
            throw new MessageException(MessageErrorCode.INVALID_EMPTY_SCHEDULE_LIST);
        }
        return values;
    }
}
