package org.terning.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.user.application.fcmtoken.update.FcmTokenUpdateService;
import org.terning.user.application.fcmtoken.validate.FcmTokenValidateService;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.request.UpdateFcmTokenRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final FcmTokenValidateService fcmTokenValidateService;
    private final FcmTokenUpdateService fcmTokenUpdateService;

    public FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request) {
        return fcmTokenValidateService.isFcmTokenReissueRequired(request);
    }

    @Transactional
    public void updateFcmToken(Long userId, UpdateFcmTokenRequest request) {
        fcmTokenUpdateService.updateFcmToken(userId, request.newToken());
    }
}
