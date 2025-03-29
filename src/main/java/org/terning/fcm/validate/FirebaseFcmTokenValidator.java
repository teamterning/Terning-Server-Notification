package org.terning.fcm.validate;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import org.springframework.stereotype.Component;
import org.terning.fcm.common.failure.FcmErrorCode;
import org.terning.fcm.common.failure.FcmException;
import org.terning.user.domain.vo.FcmToken;

@Component
public class FirebaseFcmTokenValidator implements FcmTokenValidator {

    private static final String VALIDATION_TYPE_KEY = "type";
    private static final String VALIDATION_TYPE_VALUE = "validate";

    @Override
    public boolean isExpiredWith(FcmToken token) {
        Message message = Message.builder()
                .setToken(token.value())
                .putData(VALIDATION_TYPE_KEY, VALIDATION_TYPE_VALUE)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            return false;

        } catch (FirebaseMessagingException e) {
            if (MessagingErrorCode.UNREGISTERED.equals(e.getMessagingErrorCode())) {
                return true;
            }
            throw new FcmException(FcmErrorCode.FCM_SEND_FAILURE);
        } catch (Exception e) {
            throw new FcmException(FcmErrorCode.FCM_SEND_FAILURE);
        }
    }
}
