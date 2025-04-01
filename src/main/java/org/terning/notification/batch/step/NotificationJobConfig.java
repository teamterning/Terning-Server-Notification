package org.terning.notification.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.terning.notification.batch.executor.NotificationCreationTasklet;

@Configuration
@RequiredArgsConstructor
public class NotificationJobConfig {

    public static final String NOTIFICATION_CREATION_JOB = "notificationCreationJob";
    public static final String NOTIFICATION_CREATION_STEP = "notificationCreationStep";

    private final NotificationCreationTasklet notificationCreationTasklet;

    @Bean
    public Job notificationCreationJob(
            JobRepository jobRepository,
            Step notificationCreationStep
    ) {
        return new JobBuilder(NOTIFICATION_CREATION_JOB, jobRepository)
                .start(notificationCreationStep)
                .build();
    }

    @Bean
    public Step notificationCreationStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder(NOTIFICATION_CREATION_STEP, jobRepository)
                .tasklet(notificationCreationTasklet, transactionManager)
                .build();
    }
}
