package org.terning.scrap.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.terning.scrap.dto.response.OpsScrapUsersResponse;
import org.terning.scrap.dto.request.ScrapUserIdsRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpsScrapUserClient {

    private final WebClient webClient;

    @Value("${ops.server.url}")
    private String opsBaseUrl;

    public OpsScrapUsersResponse fetchUnsyncedUsers() {
        return webClient.get()
                .uri(opsBaseUrl + "/api/v1/external/scraps/unsynced")
                .retrieve()
                .bodyToMono(OpsScrapUsersResponse.class)
                .block();
    }

    public void notifyScrapResult(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return;

        ScrapUserIdsRequest request = new ScrapUserIdsRequest(userIds);

        webClient.post()
                .uri(opsBaseUrl + "/api/v1/external/scraps/sync/result")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
