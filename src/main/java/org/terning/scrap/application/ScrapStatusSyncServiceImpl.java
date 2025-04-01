package org.terning.scrap.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.infra.ops.scrap.OpsScrapUserClient;
import org.terning.infra.ops.scrap.dto.response.OpsScrapUserResponse;

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
    }

    private List<Long> extractUserIdsFromOps() {
        return opsScrapUserClient.fetchUnsyncedUsers().users().stream()
                .map(OpsScrapUserResponse::userId)
                .toList();
    }
}