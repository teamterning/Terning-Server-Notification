package org.terning.message.domain.enums;

import org.terning.message.domain.vo.MessageWeeklyPolicy;
import org.terning.message.domain.vo.Schedule;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public enum SendSchedulePolicy {
    INTERESTED_ANNOUNCEMENT(MessageWeeklyPolicy.of(List.of(
            new Schedule(DayOfWeek.MONDAY, LocalTime.of(17, 0)))
    )),
    INTERNSHIP_RECOMMENDATION(MessageWeeklyPolicy.of(List.of(
            new Schedule(DayOfWeek.THURSDAY, LocalTime.of(13, 0)),
            new Schedule(DayOfWeek.SATURDAY, LocalTime.of(13, 0))
    ))),
    TRENDING_INTERNSHIP(MessageWeeklyPolicy.of(List.of(
            new Schedule(DayOfWeek.SUNDAY, LocalTime.of(21, 0)))
    ));

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
}
