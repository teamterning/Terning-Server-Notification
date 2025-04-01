package org.terning.scrap.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapUserSyncJobScheduler {

    private static final String LOG_JOB_SUCCESS = "[스크랩 배치] 동기화 작업 실행 완료";
    private static final String LOG_JOB_FAIL = "[스크랩 배치] 동기화 작업 실행 실패";

    private final JobLauncher jobLauncher;
    private final Job scrapSyncJob;

    @Scheduled(cron = "0 30 20 * * *")
    public void runScrapSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("run.id", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(scrapSyncJob, jobParameters);
            log.info(LOG_JOB_SUCCESS);
        } catch (Exception e) {
            log.error(LOG_JOB_FAIL, e);
        }
    }
}
