package org.terning.infra.ops.scrap.dto.response;

import java.util.List;

public record OpsScrapUsersResponse(List<OpsScrapUserResponse> users) {
    public static OpsScrapUsersResponse of(List<Long> userIds) {
        List<OpsScrapUserResponse> responses = userIds.stream()
                .map(OpsScrapUserResponse::from)
                .toList();

        return new OpsScrapUsersResponse(responses);
    }
}
