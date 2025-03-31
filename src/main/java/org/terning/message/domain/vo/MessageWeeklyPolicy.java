package org.terning.message.domain.vo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public record MessageWeeklyPolicy(DayOfWeek dayOfWeek, LocalTime time) {

    public static MessageWeeklyPolicy of(DayOfWeek dayOfWeek, LocalTime time) {
        return new MessageWeeklyPolicy(dayOfWeek, time);
    }

    public boolean isTodaySchedule(LocalDateTime now) {
        return now.getDayOfWeek() == dayOfWeek && now.toLocalTime().equals(time);
    }

    public LocalDateTime nextScheduleDate(LocalDateTime now) {
        LocalDateTime next = now.with(TemporalAdjusters.nextOrSame(dayOfWeek)).with(time);
        if (next.isBefore(now)) {
            return next.plusWeeks(1);
        }
        return next;
    }
}

