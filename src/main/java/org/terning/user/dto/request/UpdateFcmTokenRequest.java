package org.terning.user.dto.request;

public record UpdateFcmTokenRequest(String newToken) {
    public static UpdateFcmTokenRequest of(String newToken) {
        return new UpdateFcmTokenRequest(newToken);
    }
}
