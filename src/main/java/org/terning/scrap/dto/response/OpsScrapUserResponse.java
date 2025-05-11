package org.terning.scrap.dto.response;

public record OpsScrapUserResponse(Long userId) {
    public static OpsScrapUserResponse from(Long userId) {
        return new OpsScrapUserResponse(userId);
    }
}
