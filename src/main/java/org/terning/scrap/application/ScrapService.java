package org.terning.scrap.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapStatusSyncService scrapStatusSyncService;

    @Transactional
    public void fetchAndSaveScrapUsersFromOps() {
        scrapStatusSyncService.syncScrapUsersFromOps();
    }
}
