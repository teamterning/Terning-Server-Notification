package org.terning.notification.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class SentStatus {

    private boolean value;

    protected SentStatus() {
        this.value = false;
    }

    private SentStatus(boolean value) {
        this.value = value;
    }

    public static SentStatus markNotSent() {
        return new SentStatus(false);
    }

    public SentStatus markSent() {
        return new SentStatus(true);
    }

    public boolean isSent() {
        return value;
    }

    public boolean value() {
        return value;
    }
}


