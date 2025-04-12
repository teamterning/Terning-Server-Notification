package org.terning.scrap.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.terning.scrap.common.failure.ScrapErrorCode;
import org.terning.scrap.common.failure.ScrapException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapUserSyncJobScheduler {

    private static final String LOG_SUCCESS = "[스크랩 배치] 동기화 작업 실행 완료";
    private static final String RUN_ID = "run.id";
    private static final String CRON_SCHEDULE = "0 0 3 * * *";

    private final JobLauncher jobLauncher;
    private final Job scrapUserSyncJob;

    @Scheduled(cron = CRON_SCHEDULE)
    public void runScrapSyncJob() {
        try {
            jobLauncher.run(scrapUserSyncJob, new JobParametersBuilder()
                    .addLong(RUN_ID, System.currentTimeMillis())
                    .toJobParameters());
            log.info(LOG_SUCCESS);
        } catch (Exception e) {
            throw new ScrapException(ScrapErrorCode.SYNC_JOB_FAILED);
        }
    }
}
