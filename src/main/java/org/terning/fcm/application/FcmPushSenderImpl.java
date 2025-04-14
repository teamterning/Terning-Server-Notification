package org.terning.fcm.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.terning.notification.domain.Notification;
import org.terning.user.domain.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmPushSenderImpl implements FcmPushSender {

    @Override
    public boolean send(Notification notification) {
        try {
            User user = notification.getUser();
            String token = user.getToken().value();

            Message fcmMessage = Message.builder()
                    .setToken(token)
                    .putData("title", notification.getMessage().getMain())
                    .putData("body", notification.getMessage().getSub())
                    .build();

            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            log.info("FCM 메세지 전송 성공, 응답 ID: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메세지 전송 실패: {}", e.getMessage(), e);
            return false;
        }
    }
}
