package org.terning.notification.batch.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private static final String CRON_NOTIFICATION_CREATION = "0 10 3 * * *";
    private static final String JOB_PARAM_TIMESTAMP = "timestamp";

    private final JobLauncher jobLauncher;
    private final Job notificationCreationJob;

    @Scheduled(cron = CRON_NOTIFICATION_CREATION)
    public void runNotificationCreationJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(JOB_PARAM_TIMESTAMP, System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(notificationCreationJob, jobParameters);
    }
}
