package org.terning.user.dto.request;

public record FcmTokenReissueRequiredRequest(
        Long userId
) {
    public static FcmTokenReissueRequiredRequest of(Long userId) {
        return new FcmTokenReissueRequiredRequest(userId);
    }
}
