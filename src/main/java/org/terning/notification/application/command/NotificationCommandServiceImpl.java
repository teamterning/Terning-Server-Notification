package org.terning.notification.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.application.writer.NotificationWriter;
import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.notification.dto.request.NotificationCreateRequest;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationWriter notificationWriter;

    public void create(NotificationCreateRequest request, ScheduledTime scheduledTime) {
        MessageTemplateType messageTemplateType = MessageTemplateType.from(request.template());
        notificationWriter.write(messageTemplateType, scheduledTime);
    }
}
