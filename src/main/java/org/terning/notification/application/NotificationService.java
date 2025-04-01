package org.terning.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.application.command.NotificationCommandService;
import org.terning.notification.application.executor.NotificationCreationExecutor;
import org.terning.notification.dto.request.NotificationCreateRequest;
import org.terning.notification.domain.vo.ScheduledTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationCommandService notificationCommandService;
    private final NotificationCreationExecutor notificationCreationExecutor;

    public void createNotification(NotificationCreateRequest request, ScheduledTime scheduledTime) {
        notificationCommandService.create(request, scheduledTime);
    }

    public void executeNotificationCreation(MessageTemplateType template, ScheduledTime scheduledTime) {
        notificationCreationExecutor.execute(template, scheduledTime);
    }
}