package org.terning.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.terning.user.application.fcmtoken.validate.FcmTokenValidateService;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FcmTokenValidateService fcmTokenValidateService;

    public FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request) {
        return fcmTokenValidateService.isFcmTokenReissueRequired(request);
    }
}