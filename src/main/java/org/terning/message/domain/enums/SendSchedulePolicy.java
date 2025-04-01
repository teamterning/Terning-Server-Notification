package org.terning.message.domain.enums;

import org.terning.message.domain.vo.MessageWeeklyPolicy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum SendSchedulePolicy {
    INTERESTED_ANNOUNCEMENT(MessageWeeklyPolicy.of(DayOfWeek.MONDAY, LocalTime.of(21, 0))),
    INTERNSHIP_RECOMMENDATION(MessageWeeklyPolicy.of(DayOfWeek.WEDNESDAY, LocalTime.of(20, 30))),
    TRENDING_INTERNSHIP(MessageWeeklyPolicy.of(DayOfWeek.FRIDAY, LocalTime.of(18, 0)));

    private final MessageWeeklyPolicy schedule;

    SendSchedulePolicy(MessageWeeklyPolicy schedule) {
        this.schedule = schedule;
    }

    public boolean isDue(LocalDateTime now) {
        return schedule.isTodaySchedule(now);
    }

    public LocalDateTime nextScheduleAfter(LocalDateTime now) {
        return schedule.nextScheduleDate(now);
    }

    public DayOfWeek day() {
        return schedule.dayOfWeek();
    }

    public LocalTime time() {
        return schedule.time();
    }
}


