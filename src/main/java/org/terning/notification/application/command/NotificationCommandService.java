package org.terning.notification.application.command;

import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.notification.dto.request.NotificationCreateRequest;

public interface NotificationCommandService {
    void create(NotificationCreateRequest request, ScheduledTime scheduledTime);
}

