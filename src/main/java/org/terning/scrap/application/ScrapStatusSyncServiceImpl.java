package org.terning.scrap.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.scrap.config.OpsScrapUserClient;
import org.terning.scrap.dto.response.OpsScrapUserResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapStatusSyncServiceImpl implements ScrapStatusSyncService {

    private final OpsScrapUserClient opsScrapUserClient;
    private final ScrapSyncManager scrapSyncManager;

    @Override
    public void syncScrapUsersFromOps() {
        List<Long> userIds = extractUserIdsFromOps();
        if (userIds.isEmpty()) return;

        scrapSyncManager.sync(userIds);

        opsScrapUserClient.notifyScrapResult(userIds);
    }

    private List<Long> extractUserIdsFromOps() {
        return opsScrapUserClient.fetchUnsyncedUsers().users().stream()
                .map(OpsScrapUserResponse::userId)
                .toList();
    }
}