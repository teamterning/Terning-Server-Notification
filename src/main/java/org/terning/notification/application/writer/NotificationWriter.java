package org.terning.notification.application.writer;

import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.domain.vo.ScheduledTime;

public interface NotificationWriter {
    void write(MessageTemplateType template, ScheduledTime sendTime);
}
