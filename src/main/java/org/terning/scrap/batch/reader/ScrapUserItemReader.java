package org.terning.scrap.batch.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.terning.scrap.config.OpsScrapUserClient;
import org.terning.scrap.dto.response.OpsScrapUserResponse;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapUserItemReader implements ItemReader<OpsScrapUserResponse> {

    private static final String LOG_FETCHING_USERS = "[스크랩 배치] 동기화 대상 유저 목록 조회 시작";

    private final OpsScrapUserClient opsScrapUserClient;
    private Iterator<OpsScrapUserResponse> userIterator;

    @Override
    public OpsScrapUserResponse read() {
        if (userIterator == null) {
            log.info(LOG_FETCHING_USERS);
            List<OpsScrapUserResponse> users = opsScrapUserClient.fetchUnsyncedUsers().users();
            userIterator = users.iterator();
        }

        if (userIterator.hasNext()) {
            return userIterator.next();
        }

        return null;
    }
}
