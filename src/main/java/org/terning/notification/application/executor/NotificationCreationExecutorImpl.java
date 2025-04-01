package org.terning.notification.application.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.application.writer.NotificationWriter;
import org.terning.notification.domain.vo.ScheduledTime;

@Service
@RequiredArgsConstructor
public class NotificationCreationExecutorImpl implements NotificationCreationExecutor {

    private final NotificationWriter notificationWriter;

    @Override
    public void execute(MessageTemplateType template, ScheduledTime scheduledTime) {
        notificationWriter.write(template, scheduledTime);
    }
}

