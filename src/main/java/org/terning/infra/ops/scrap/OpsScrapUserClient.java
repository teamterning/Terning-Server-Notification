package org.terning.infra.ops.scrap;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.terning.infra.ops.scrap.dto.response.OpsScrapUsersResponse;

@Component
@RequiredArgsConstructor
public class OpsScrapUserClient {

    private final WebClient webClient;

    @Value("${internal.api.scrap.unsynced}")
    private String opsScrapUserUrl;

    public OpsScrapUsersResponse fetchUnsyncedUsers() {
        return webClient.get()
                .uri(opsScrapUserUrl)
                .retrieve()
                .bodyToMono(OpsScrapUsersResponse.class)
                .block();
    }
}
