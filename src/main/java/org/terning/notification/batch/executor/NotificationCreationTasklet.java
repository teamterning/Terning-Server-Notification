package org.terning.notification.batch.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.application.NotificationService;
import org.terning.notification.domain.vo.ScheduledTime;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class NotificationCreationTasklet implements Tasklet {

    private final NotificationService notificationService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LocalDateTime now = LocalDateTime.now();
        for (MessageTemplateType template : MessageTemplateType.values()) {
            ScheduledTime scheduledTime = ScheduledTime.of(now);
            notificationService.executeNotificationCreation(template, scheduledTime);
        }

        return RepeatStatus.FINISHED;
    }
}
