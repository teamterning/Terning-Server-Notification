package org.terning.user.dto.response;

public record FcmTokenReissueRequiredResponse(
        boolean reissueRequired
) {
    public static FcmTokenReissueRequiredResponse of(boolean reissueRequired) {
        return new FcmTokenReissueRequiredResponse(reissueRequired);
    }
}