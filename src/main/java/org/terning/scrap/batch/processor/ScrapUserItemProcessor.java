package org.terning.scrap.batch.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.terning.scrap.dto.response.OpsScrapUserResponse;

@Slf4j
@Component
public class ScrapUserItemProcessor implements ItemProcessor<OpsScrapUserResponse, OpsScrapUserResponse> {

    private static final String LOG_PROCESSING_USER = "[스크랩 배치] 처리 대상 유저 ID: {}";

    @Override
    public OpsScrapUserResponse process(OpsScrapUserResponse item) {
        log.debug(LOG_PROCESSING_USER, item.userId());
        return item;
    }
}