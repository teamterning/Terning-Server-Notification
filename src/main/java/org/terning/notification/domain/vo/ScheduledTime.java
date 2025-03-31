package org.terning.notification.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
public class ScheduledTime {

    private LocalDateTime value;

    protected ScheduledTime() {
        this.value = LocalDateTime.now();
    }

    private ScheduledTime(LocalDateTime value) {
        this.value = value;
    }

    public static ScheduledTime of(LocalDateTime time) {
        return new ScheduledTime(time);
    }

    public boolean isDue(LocalDateTime now) {
        return !now.isBefore(value);
    }

    public LocalDateTime value() {
        return value;
    }
}

