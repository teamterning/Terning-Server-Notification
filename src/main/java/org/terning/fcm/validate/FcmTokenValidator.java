package org.terning.fcm.validate;

import org.terning.user.domain.vo.FcmToken;

public interface FcmTokenValidator {
    boolean isExpiredWith(FcmToken token);
}

