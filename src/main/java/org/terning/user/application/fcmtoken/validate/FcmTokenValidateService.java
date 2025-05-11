package org.terning.user.application.fcmtoken.validate;

import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

public interface FcmTokenValidateService {
    FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request);
}