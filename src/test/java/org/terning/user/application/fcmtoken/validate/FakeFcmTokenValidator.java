package org.terning.user.application.fcmtoken.validate;

import org.terning.fcm.validate.FcmTokenValidator;
import org.terning.user.domain.vo.FcmToken;

public class FakeFcmTokenValidator implements FcmTokenValidator {
    private boolean expired;

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean isExpiredWith(FcmToken token) {
        return expired;
    }
}
