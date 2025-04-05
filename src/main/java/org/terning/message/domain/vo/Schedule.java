package org.terning.message.domain.vo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public record Schedule(DayOfWeek dayOfWeek, LocalTime time) {

    public boolean matches(LocalDateTime now) {
        return now.getDayOfWeek() == dayOfWeek && now.toLocalTime().equals(time);
    }

    public LocalDateTime nextScheduleAfter(LocalDateTime now) {
        LocalDateTime next = now.with(TemporalAdjusters.nextOrSame(dayOfWeek)).with(time);
        if (next.isBefore(now)) {
            return next.plusWeeks(1);
        }
        return next;
    }
}
