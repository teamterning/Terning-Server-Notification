package org.terning.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.terning.global.entity.BaseEntity;
import org.terning.message.domain.Message;
import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.notification.domain.vo.SentStatus;
import org.terning.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "schedule"))
    private ScheduledTime schedule;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sent_status"))
    private SentStatus sentStatus;

    private Notification(User user, Message message, ScheduledTime schedule) {
        this.user = user;
        this.message = message;
        this.schedule = schedule;
        this.sentStatus = SentStatus.markNotSent();
    }

    public static Notification of(User user, Message message, ScheduledTime schedule) {
        return new Notification(user, message, schedule);
    }

    public void markSent() {
        this.sentStatus = this.sentStatus.markSent();
    }

    public boolean isSent() {
        return sentStatus.isSent();
    }

    public boolean isDue(LocalDateTime now) {
        return schedule.isDue(now);
    }
}
