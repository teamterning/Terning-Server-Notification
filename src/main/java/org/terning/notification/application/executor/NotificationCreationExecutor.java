package org.terning.notification.application.executor;

import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.domain.vo.ScheduledTime;

public interface NotificationCreationExecutor {
    void execute(MessageTemplateType template, ScheduledTime scheduledTime);
}

