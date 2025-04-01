package org.terning.scrap.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.terning.scrap.batch.processor.ScrapUserItemProcessor;
import org.terning.scrap.batch.reader.ScrapUserItemReader;
import org.terning.scrap.batch.step.ScrapSyncWriter;
import org.terning.infra.ops.scrap.dto.response.OpsScrapUserResponse;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ScrapUserSyncJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ScrapUserItemReader scrapUserItemReader;
    private final ScrapUserItemProcessor scrapUserItemProcessor;
    private final ScrapSyncWriter scrapSyncWriter;

    @Bean
    public Job scrapUserSyncJob() {
        return new JobBuilder("scrapUserSyncJob", jobRepository)
                .start(scrapUserSyncStep())
                .build();
    }

    @Bean
    public Step scrapUserSyncStep() {
        return new StepBuilder("s crapUserSyncStep", jobRepository)
                .<OpsScrapUserResponse, OpsScrapUserResponse>chunk(100, transactionManager)
                .reader(scrapUserItemReader)
                .processor(scrapUserItemProcessor)
                .writer(scrapSyncWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }
}
