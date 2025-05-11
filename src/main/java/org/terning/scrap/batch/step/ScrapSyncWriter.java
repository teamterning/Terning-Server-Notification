package org.terning.scrap.batch.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.terning.scrap.dto.response.OpsScrapUserResponse;
import org.terning.scrap.application.ScrapSyncManager;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapSyncWriter implements ItemWriter<OpsScrapUserResponse> {

    private static final String LOG_WRITING_USERS = "[스크랩 배치] 동기화 대상 유저 수: {}";

    private final ScrapSyncManager scrapSyncManager;

    @Override
    public void write(Chunk<? extends OpsScrapUserResponse> chunk) {
        List<Long> userIds = extractUserIdsFrom(chunk);
        log.info(LOG_WRITING_USERS, userIds.size());
        scrapSyncManager.sync(userIds);
    }

    private List<Long> extractUserIdsFrom(Chunk<? extends OpsScrapUserResponse> chunk) {
        return StreamSupport.stream(chunk.spliterator(), false)
                .map(OpsScrapUserResponse::userId)
                .distinct()
                .toList();
    }
}
