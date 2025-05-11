package org.terning.user.application.fcmtoken.update;

public interface FcmTokenUpdateService {
    void updateFcmToken(Long userId, String newToken);
}
